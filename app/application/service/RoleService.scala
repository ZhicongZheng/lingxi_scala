package application.service

import application.command.{CreateRoleCommand, UpdateRoleCommand}
import common._
import domain.auth.RoleRepository
import domain.user.UserRepository
import infra.db.repository.RoleQueryRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RoleService @Inject() (
  roleQueryRepository: RoleQueryRepository,
  userAggregateRepository: UserRepository,
  roleAggregateRepository: RoleRepository
) {

  def changeUserRole(userId: Long, roleId: Long): Future[Either[Errors, Unit]] = {
    val userRoleOpt = for {
      userOpt <- userAggregateRepository.get(userId)
      roleOpt <- roleAggregateRepository.get(roleId)
    } yield (userOpt, roleOpt)
    userRoleOpt.flatMap {
      case (Some(user), Some(r)) =>
        userAggregateRepository.save(user.changeRole(r)).map(_ => Right(()))
      case (_, None) => Future.successful(Left(NO_ROLE))
      case _         => Future.successful(Left(NO_USER))
    }
  }

  def createRole(request: CreateRoleCommand): Future[Either[Errors, Long]] =
    roleQueryRepository.findByCode(request.code) flatMap {
      case Some(_) => Future.successful(Left(ROLE_CODE_EXIST))
      case None    => roleAggregateRepository.save(request).map(id => Right(id))
    }

  def updateRole(request: UpdateRoleCommand): Future[Either[Errors, Unit]] =
    roleAggregateRepository.get(request.id) flatMap {
      case None => Future.successful(Left(NO_ROLE))
      case Some(role) =>
        roleAggregateRepository.save(role.update(request.name, request.permissions)).map(_ => Right(()))
    }

  def deleteRole(id: Int): Future[Either[Errors, Unit]] =
    roleAggregateRepository.get(id).flatMap {
      case None                            => Future.successful(Right(()))
      case Some(role) if role.beSuperAdmin => Future.successful(Left(CAN_NOT_DEL_SUPER_ADMIN))
      case _                               => roleAggregateRepository.remove(id).map(_ => Right(()))
    }

}
