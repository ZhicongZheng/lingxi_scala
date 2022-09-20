package common

import auth.domain.User
import play.api.libs.json.Json
import play.api.mvc.Results.{Ok, Unauthorized}
import play.api.mvc.{ActionBuilder, AnyContent, BodyParser, Request, Result, WrappedRequest}
import utils.Jwt

import scala.concurrent.{ExecutionContext, Future}

case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

object JWTAuthentication extends ActionBuilder[UserRequest, AnyContent] {

  def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {

    implicit val req = request

    val jwtToken = request.headers.get("jw_token").getOrElse("")

    if (Jwt.isValidToken(jwtToken)) {
      Jwt.decodePayload(jwtToken).fold(Future.successful(Unauthorized("Invalid credential"))) { payload =>

        val userId: Long = Json.parse(payload).validate[Long].get

        // Replace this block with data source
        if (true) {
          Future.successful(Ok("Authorization successful"))
        } else {
          Future.successful(Unauthorized("Invalid credential"))
        }
      }
    } else {
      Future.successful(Unauthorized("Invalid credential"))
    }
  }

  override def parser: BodyParser[AnyContent] = ???

  override protected def executionContext: ExecutionContext = ???
}
