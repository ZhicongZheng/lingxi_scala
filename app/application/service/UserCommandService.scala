package application.service

import application.command.{ChangePasswordCommand, CreateUserCommand, LoginCommand, UpdateUserCommand}
import common.{Errors, NO_USER, OLD_PWD_ERROR, USER_EXIST}
import domain.user.repository.UserRepository
import infra.db.repository.UserQueryRepository
import play.api.mvc.{DefaultSessionCookieBaker, JWTCookieDataCodec}
import infra.db.assembler.UserAssembler._

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserCommandService @Inject() (
  userQueryRepository: UserQueryRepository,
  userAggregateRepository: UserRepository,
  private val defaultSessionCookieBaker: DefaultSessionCookieBaker
) {
  private val jwt: JWTCookieDataCodec = defaultSessionCookieBaker.jwtCodec

  def login(loginRequest: LoginCommand): Future[Either[Errors, String]] =
    userQueryRepository.findByUsername(loginRequest.username).map(toDoOpt) map {
      case Some(user) => user.login(loginRequest.password, jwt)
      case None       => Left(NO_USER)
    }

  def deleteUser(id: Int): Future[Unit] = userAggregateRepository.remove(id)

  def createUser(request: CreateUserCommand): Future[Either[Errors, Long]] =
    userQueryRepository.findByUsername(request.username) flatMap {
      case Some(_) => Future.successful(Left(USER_EXIST))
      case None    => userAggregateRepository.save(request).map(id => Right(id))
    }

  def updateUser(command: UpdateUserCommand): Future[Either[Errors, Unit]] =
    userQueryRepository.get(command.id) flatMap {
      case None       => Future.successful(Left(NO_USER))
      case Some(user) => userAggregateRepository.save(user.update(command)).map(_ => Right(()))
    }

  def changePwd(userId: Long, request: ChangePasswordCommand): Future[Either[Errors, Unit]] =
    userAggregateRepository.get(userId) flatMap {
      case None => Future.successful(Left(NO_USER))
      case Some(user) =>
        if (user.checkPwd(request.oldPassword)) {
          userAggregateRepository.save(user.changePwd(request.newPassword)).map(_ => Right(()))
        } else Future.successful(Left(OLD_PWD_ERROR))
    }

}
