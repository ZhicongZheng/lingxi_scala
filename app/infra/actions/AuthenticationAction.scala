package infra.actions

import common.{Constant, TOKEN_CHECK_ERROR}
import domain.user.entity.User
import infra.actions.AuthenticationAction.{noAuthRoute, tokenValidateError}
import play.api.libs.json.Json
import play.api.mvc._
import play.api.Logging

import java.util.regex.Pattern
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

@Singleton
class AuthenticationAction @Inject() (parser: BodyParsers.Default)(implicit
  ec: ExecutionContext
) extends ActionBuilder[UserRequest, AnyContent]
    with Logging {

  def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {

    def success(user: User) = block.apply(UserRequest(user, request))

    implicit val path: String = request.path
    if ("/" == path || noAuthRoute.exists(p => p.matcher(path).find())) {
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

  val noAuthRoute: Seq[Pattern] = Seq(
    Pattern.compile("/users/login"),
    Pattern.compile("/users/logout"),
    Pattern.compile("/docs/*"),
    Pattern.compile("/*.ico")
  )

  private def tokenValidateError(implicit path: String) = {
    logger.info(s"request path : $path, jwt token in cookie validate fail")
    Future.successful(common.Results.fail(TOKEN_CHECK_ERROR))
  }
}
