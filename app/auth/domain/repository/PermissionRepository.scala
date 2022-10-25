package auth.domain.repository

import auth.domain.Permission

import scala.concurrent.Future

trait PermissionRepository extends BaseRepository[Permission] {

  def findByRoleId(id: Long): Future[Seq[Permission]]
}
