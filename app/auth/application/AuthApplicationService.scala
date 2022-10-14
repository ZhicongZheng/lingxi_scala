package auth.application

import auth.application.dto.{ CreateUserRequest, LoginRequest, UserDto }
import auth.domain.UserRepository
import common.result.{ Errors, NO_USER, USER_EXIST }
import common.{ PageDto, PageQuery }
import play.api.mvc.{ DefaultSessionCookieBaker, JWTCookieDataCodec }

import javax.inject.{ Inject, Singleton }
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

  def deleteUser(id: Int): Future[Int] = userRepository.delete(id)

  def createUser(request: CreateUserRequest): Future[Either[Errors, Long]] =
    userRepository.findByUsername(request.username) flatMap {
      case Some(_) => Future.successful(Left(USER_EXIST))
      case None =>
        userRepository.create(request).map(id => Right(id))
    }

}
