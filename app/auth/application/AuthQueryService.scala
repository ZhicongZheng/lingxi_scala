package auth.application

import auth.application.dto.{RoleDto, UserDto}
import auth.domain.repository.{PermissionRepository, RoleRepository, UserRepository}
import auth.domain.User
import common.{PageDto, PageQuery}
import play.api.Logging

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class AuthQueryService @Inject() (
  userRepository: UserRepository,
  roleRepository: RoleRepository,
  permissionRepository: PermissionRepository
) extends Logging {

  def listUserByPage(pageQuery: PageQuery): Future[PageDto[UserDto]] = userRepository.listByPage(pageQuery).map(_.map(UserDto.fromDo))

  def listRolesByPage(pageQuery: PageQuery): Future[PageDto[RoleDto]] = roleRepository.listByPage(pageQuery).map(_.map(RoleDto.formDo))

  def richUser(user: User): Future[User] =
    for {
      role        <- roleRepository.findByUserId(user.id)
      permissions <- if (role.isEmpty) Future.successful(Nil) else permissionRepository.findByRoleId(role.get.id)
    } yield user.copy(role = role, permissions = permissions)

}
