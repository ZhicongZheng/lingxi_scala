package auth.application

import auth.application.dto.{ChangePasswordRequest, CreateRoleRequest, CreateUserRequest, LoginRequest, UpdateRoleRequest}
import auth.domain.User
import auth.domain.repository.{RoleRepository, UserRepository}
import common.result._
import common.Constant.superAdmin
import play.api.mvc.{DefaultSessionCookieBaker, JWTCookieDataCodec}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class AuthApplicationService @Inject() (
  val userRepository: UserRepository,
  val roleRepository: RoleRepository,
  val defaultSessionCookieBaker: DefaultSessionCookieBaker
) {

  val jwt: JWTCookieDataCodec = defaultSessionCookieBaker.jwtCodec

  def login(loginRequest: LoginRequest): Future[Either[Errors, String]] =
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

  def changePwd(userId: Long, request: ChangePasswordRequest): Future[Either[Errors, Unit]] =
    userRepository.findById(userId) flatMap {
      case None => Future.successful(Left(NO_USER))
      case Some(user) =>
        if (user.checkPwd(request.oldPassword)) {
          userRepository.update(user.copy(password = User.entryPwd(request.newPassword))).map(_ => Right(()))
        } else Future.successful(Left(OLD_PWD_ERROR))
    }

  def changeUserRole(userId: Long, roleId: Long): Future[Either[Errors, Unit]] = {
    val userRoleOpt = for {
      userOpt <- userRepository.findById(userId)
      roleOpt <- roleRepository.findById(roleId)
    } yield (userOpt, roleOpt)
    userRoleOpt.flatMap {
      case (Some(user), Some(r)) =>
        userRepository.changeRole(user.copy(role = Some(r))).map(_ => Right(()))
      case (None, _) => Future.successful(Left(NO_USER))
      case (_, None) => Future.successful(Left(NO_ROLE))
      case _         => Future.successful(Left(NO_USER))
    }
  }

  def createRole(request: CreateRoleRequest): Future[Long] = roleRepository.create(request)

  def updateRole(request: UpdateRoleRequest): Future[Either[Errors, Int]] = {
    roleRepository.findById(request.id) flatMap {
      case None => Future.successful(Left(NO_ROLE))
      case Some(role) =>
        roleRepository.update(role.update(request)).map(c => Right(c))
    }
    Future.successful(Right(1))
  }

  def deleteRole(id: Int): Future[Either[Errors, Int]] =
    roleRepository.findById(id).flatMap {
      case None                                  => Future.successful(Right(0))
      case Some(role) if role.code == superAdmin => Future.successful(Left(CAN_NOT_DEL_SUPER_ADMIN))
      case _                                     => roleRepository.delete(id).map(delCount => Right(delCount))
    }

}
