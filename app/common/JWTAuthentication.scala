package common

import auth.domain.{User, UserRepository}
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.mvc.Results.{Ok, Unauthorized}
import play.api.mvc.{ActionBuilder, AnyContent, BodyParser, BodyParsers, Request, Result, WrappedRequest}
import utils.Jwt

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future, blocking}

case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

class JWTAuthentication @Inject()(parser: BodyParsers.Default)
                                 (implicit ec: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent] {

  def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {

    val jwtToken: String = request.headers.get(HeaderNames.AUTHORIZATION).getOrElse("")

    if (Jwt.isValidToken(jwtToken)) {
      Jwt.decodePayload(jwtToken).fold(Future.successful(Unauthorized("Invalid credential"))) { payload: String =>

        val userId = Json.parse(payload).validate[Long].asOpt.get

        // Replace this block with data source
        if (userId > 1) {
          Future.successful(Ok("Authorization successful"))
        } else {
          Future.successful(Unauthorized("Invalid credential"))
        }
      }
    } else {
      Future.successful(Unauthorized("Invalid credential"))
    }
  }

  override def parser: BodyParser[AnyContent] = parser

  override protected def executionContext: ExecutionContext = ec
}
