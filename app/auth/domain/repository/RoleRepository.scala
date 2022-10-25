package auth.domain.repository

import auth.domain.{BaseInfo, Role}

import scala.concurrent.Future

trait RoleRepository extends BaseRepository[BaseInfo] {

  def findByUserId(userId: Long): Future[Option[Role]]

}
