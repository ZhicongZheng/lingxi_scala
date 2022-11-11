package domain.auth.repository

import domain.BaseRepository
import domain.auth.value_obj.Permission

import scala.concurrent.Future

trait PermissionRepository extends BaseRepository[Permission] {

  def findByRoleId(id: Long): Future[Seq[Permission]]
}
