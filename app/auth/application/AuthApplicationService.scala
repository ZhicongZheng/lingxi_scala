package auth.application

import auth.application.dto.LoginRequest
import auth.domain.UserRepository
import common.ResponseCode
import common.exceptions.BizException
import play.api.mvc.{DefaultSessionCookieBaker, JWTCookieDataCodec}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class AuthApplicationService @Inject()(val userRepository: UserRepository,
                                       val defaultSessionCookieBaker: DefaultSessionCookieBaker) {

  val jwt: JWTCookieDataCodec = defaultSessionCookieBaker.jwtCodec

  def login(loginRequest: LoginRequest): Future[String] = {
    userRepository.findByUsername(loginRequest.username) map {
      case Some(user) => user.login(loginRequest.password, jwt)
      case None => throw new BizException(ResponseCode.NO_USER)
    }
  }

}
