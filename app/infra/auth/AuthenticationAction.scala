package infra.auth

import common.{Constant, TOKEN_CHECK_ERROR}
import domain.user.User
import infra.auth.AuthenticationAction.{tokenValidateError, withoutAuth}
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc._
import play.api.routing.{HandlerDef, Router}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex

case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

@Singleton
class AuthenticationAction @Inject() (parser: BodyParsers.Default)(implicit
  ec: ExecutionContext
) extends ActionBuilder[UserRequest, AnyContent]
    with Logging {

  def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {

    def success(user: User) = block.apply(UserRequest(user, request))

    implicit val path: String = request.path
    if (withoutAuth(request)) {
      return success(null)
    }

    request.session.get(Constant.SESSION_USER) match {
      case Some(sessionUser) => success(Json.fromJson[User](Json.parse(sessionUser)).get)
      case None              => tokenValidateError
    }

  }

  override def parser: BodyParser[AnyContent] = parser

  override protected def executionContext: ExecutionContext = ec
}

object AuthenticationAction extends Logging {

  private final case class WithoutAuthRoute(method: Regex, path: Regex) {
    def matches(handlerDef: HandlerDef): Boolean = {
      val method = handlerDef.verb
      val path   = handlerDef.path

      this.path.matches(path) && this.method.matches(method)
    }
  }

  lazy private val withoutAuthRouteList: Seq[WithoutAuthRoute] = Seq(
    WithoutAuthRoute("POST".r, "/users/login.*".r),
    WithoutAuthRoute("POST".r, "/users/logout".r),
    WithoutAuthRoute(".*".r, "/docs/*".r),
    WithoutAuthRoute(".*".r, "/*.ico".r)
  )

  def withoutAuth[A](request: Request[A]): Boolean = {
    val handlerDefOpt                                = request.attrs.get(Router.Attrs.HandlerDef)
    def withoutAuth(handlerDef: HandlerDef): Boolean = withoutAuthRouteList.map(_.matches(handlerDef)).reduce((a, b) => a && b)
    handlerDefOpt.exists(withoutAuth)
  }

  private def tokenValidateError(implicit path: String) = {
    logger.info(s"request path : $path, jwt token in cookie validate fail")
    Future.successful(common.Results.fail(TOKEN_CHECK_ERROR))
  }
}
