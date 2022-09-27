package common

import auth.domain.{User, UserRepository}
import play.api.http.HeaderNames
import play.api.mvc.Results.{Ok, Unauthorized}
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

class JWTAuthentication @Inject()(parser: BodyParsers.Default,
                                  defaultSessionCookieBaker: DefaultSessionCookieBaker,
                                  userRepository: UserRepository)
                                 (implicit ec: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent] {

  val jwt: JWTCookieDataCodec = defaultSessionCookieBaker.jwtCodec

  def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {

    val jwtToken: String = request.headers.get(HeaderNames.AUTHORIZATION).getOrElse("")


    Try(jwt.decode(jwtToken)) match {
      case Success(claim) =>
        claim.get("id").map { userId =>
          userRepository.findById(userId.toLong).flatMap {
            case Some(_) => Future.successful(Ok("Authorization successful"))
            case None => Future.successful(Unauthorized("Invalid credential"))
          }
        }.getOrElse(Future.successful(Unauthorized("Invalid credential")))
      case Failure(_) => Future.successful(Unauthorized("Invalid credential"))
    }
  }

  override def parser: BodyParser[AnyContent] = parser

  override protected def executionContext: ExecutionContext = ec
}
