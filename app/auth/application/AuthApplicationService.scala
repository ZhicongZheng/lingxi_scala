package auth.application

import auth.application.dto.LoginRequest
import auth.domain.UserRepository
import common.ResponseCode
import common.exceptions.BizException
import org.mindrot.jbcrypt.BCrypt
import play.api.mvc.{DefaultSessionCookieBaker, JWTCookieDataCodec}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

@Singleton
class AuthApplicationService @Inject() (val userRepository: UserRepository,
                                        val defaultSessionCookieBaker: DefaultSessionCookieBaker) {

  val jwt: JWTCookieDataCodec = defaultSessionCookieBaker.jwtCodec

  def login(loginRequest: LoginRequest): Future[String] = {
    userRepository.findByUsername(loginRequest.username).flatMap {
      case Some(user) =>
        Try(BCrypt.checkpw(loginRequest.password, user.password)) match {
          case Success(res) if res => Future.successful(jwt.encode(Map("id" -> user.id.toString)))
          case Success(_) => Future.failed(new BizException(ResponseCode.LOGIN_FAILED))
          case Failure(_) => Future.failed(new BizException(ResponseCode.LOGIN_FAILED))
        }
      case None => Future.failed(new BizException(ResponseCode.NO_USER))
    }
  }

}
