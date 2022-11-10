package application.command

import common._
import common.Constant.superAdmin
import domain.auth.repository.RoleRepository
import domain.user.repository.UserRepository
import interfaces.dto.{CreateRoleRequest, UpdateRoleRequest}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class AuthCommandService @Inject() (private val userRepository: UserRepository, private val roleRepository: RoleRepository) {

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

  def createRole(request: CreateRoleRequest): Future[Either[Errors, Long]] =
    roleRepository.findByCode(request.code) flatMap {
      case Some(_) => Future.successful(Left(ROLE_CODE_EXIST))
      case None    => roleRepository.create(request).map(id => Right(id))
    }

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
