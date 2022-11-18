package application.service

import common.{Page, PageQuery}
import domain.auth.repository.{PermissionRepository, RoleRepository}
import domain.user.entity.User
import interfaces.dto.RoleDto
import play.api.Logging

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class AuthQueryService @Inject() (private val roleRepository: RoleRepository) extends Logging {

  def listRolesByPage(pageQuery: PageQuery): Future[Page[RoleDto]] = roleRepository.listByPage(pageQuery).map(_.map(RoleDto.formDo))

}
