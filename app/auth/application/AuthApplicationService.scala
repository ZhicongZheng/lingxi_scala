package auth.application

import auth.application.dto.{LoginRequest, UserDto}
import auth.domain.UserRepository
import common.{PageDto, PageQuery}
import common.result.{Errors, NO_USER}
import play.api.mvc.{DefaultSessionCookieBaker, JWTCookieDataCodec}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class AuthApplicationService @Inject() (
  val userRepository: UserRepository,
  val defaultSessionCookieBaker: DefaultSessionCookieBaker
) {

  val jwt: JWTCookieDataCodec = defaultSessionCookieBaker.jwtCodec

  def login(loginRequest: LoginRequest): Future[Either[Errors, String]] =
    userRepository.findByUsername(loginRequest.username) map {
      case Some(user) => user.login(loginRequest.password, jwt)
      case None       => Left(NO_USER)
    }

  def listByPage(pageQuery: PageQuery): Future[PageDto[UserDto]] = userRepository.listByPage(pageQuery).map(_.map(UserDto.fromDo))

}
