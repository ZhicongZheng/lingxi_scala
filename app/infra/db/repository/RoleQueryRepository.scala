package infra.db.repository

import infra.db.po.{PermissionPo, RolePo}

import scala.concurrent.Future

trait RoleQueryRepository extends QueryRepository[RolePo] {

  def findByCode(code: String): Future[Option[RolePo]]

  def listPermissions(): Future[Seq[PermissionPo]]
}
