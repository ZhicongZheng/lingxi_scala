package application.service

import common.{BasePageQuery, Page}
import infra.db.po.PermissionPo
import infra.db.repository.RoleQueryRepository
import interfaces.dto.{PermissionDto, RoleDto}
import play.api.Logging

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RoleQueryService @Inject() (roleQueryRepository: RoleQueryRepository) extends Logging {

  def listRolesByPage(pageQuery: BasePageQuery): Future[Page[RoleDto]] =
    roleQueryRepository.listByPage(pageQuery).flatMap { rolePage =>
      val roleMap = rolePage.data.map(role => role.id -> role).toMap
      val roleIds = roleMap.keys.toSeq
      roleQueryRepository.findRolePermissionMap(roleIds).map { map =>
        val roleDtos = roleMap.map { tuple =>
          val permissions = map.get(tuple._1).map(ps => ps.map(PermissionDto.fromPo)).getOrElse(Nil)
          RoleDto.fromPo(tuple._2).copy(permissions = permissions)
        }
        rolePage.copy(data = roleDtos.toSeq)
      }
    }

  def listPermission(): Future[Seq[PermissionPo]] = roleQueryRepository.listPermissions();

}
