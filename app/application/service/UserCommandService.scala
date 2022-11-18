package application.service

import application.command.{ChangePasswordCommand, CreateUserRequest, LoginCommand}
import common.{Errors, NO_USER, OLD_PWD_ERROR, USER_EXIST}
import domain.user.entity.User
import domain.user.repository.UserRepository
import play.api.mvc.{DefaultSessionCookieBaker, JWTCookieDataCodec}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserCommandService @Inject() (
  private val userRepository: UserRepository,
  private val defaultSessionCookieBaker: DefaultSessionCookieBaker
) {
  private val jwt: JWTCookieDataCodec = defaultSessionCookieBaker.jwtCodec

  def login(loginRequest: LoginCommand): Future[Either[Errors, String]] =
    userRepository.findByUsername(loginRequest.username) map {
      case Some(user) => user.login(loginRequest.password, jwt)
      case None       => Left(NO_USER)
    }

  def deleteUser(id: Int): Future[Int] = userRepository.delete(id)

  def createUser(request: CreateUserRequest): Future[Either[Errors, Long]] =
    userRepository.findByUsername(request.username) flatMap {
      case Some(_) => Future.successful(Left(USER_EXIST))
      case None    => userRepository.create(request).map(id => Right(id))
    }

  def changePwd(userId: Long, request: ChangePasswordCommand): Future[Either[Errors, Unit]] =
    userRepository.findById(userId) flatMap {
      case None => Future.successful(Left(NO_USER))
      case Some(user) =>
        if (user.checkPwd(request.oldPassword)) {
          userRepository.update(user.copy(password = User.entryPwd(request.newPassword))).map(_ => Right(()))
        } else Future.successful(Left(OLD_PWD_ERROR))
    }

}
