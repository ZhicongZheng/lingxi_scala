package auth.domain.repository

import auth.domain.{BaseInfo, Role}

import scala.concurrent.Future

trait RoleRepository extends BaseRepository[Role] {

  def findByUserId(userId: Long): Future[Option[Role]]

}
