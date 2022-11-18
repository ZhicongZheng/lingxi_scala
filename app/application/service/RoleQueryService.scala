package application.service

import common.{Page, PageQuery}
import infra.db.po.PermissionPo
import infra.db.repository.RoleQueryRepository
import interfaces.dto.RoleDto
import play.api.Logging

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RoleQueryService @Inject() (roleQueryRepository: RoleQueryRepository) extends Logging {

  def listRolesByPage(pageQuery: PageQuery): Future[Page[RoleDto]] = roleQueryRepository.listByPage(pageQuery).map(_.map(RoleDto.fromPo))

  def listPermission(): Future[Seq[PermissionPo]] = roleQueryRepository.listPermissions();

}
