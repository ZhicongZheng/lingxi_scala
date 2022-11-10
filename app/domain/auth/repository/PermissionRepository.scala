package domain.auth.repository

import domain.auth.entity.Permission
import domain.BaseRepository

import scala.concurrent.Future

trait PermissionRepository extends BaseRepository[Permission] {

  def findByRoleId(id: Long): Future[Seq[Permission]]
}
