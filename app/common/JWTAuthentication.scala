package common

import auth.domain.User
import play.api.libs.json.Json
import play.api.mvc.Results.{Ok, Unauthorized}
import play.api.mvc.{ActionBuilder, AnyContent, BodyParser, BodyParsers, Request, Result, WrappedRequest}
import utils.Jwt

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

class JWTAuthentication @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent] {

  def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {

    val jwtToken: String = request.headers.get("jw_token").getOrElse("")

    if (Jwt.isValidToken(jwtToken)) {
      Jwt.decodePayload(jwtToken).fold(Future.successful(Unauthorized("Invalid credential"))) { payload: String =>

        val userId: Long = Json.parse(payload).validate[Long].get

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
