package auth.domain

import auth.domain.repository.BaseRepository

import scala.concurrent.Future

trait RoleRepository extends BaseRepository[BaseInfo] {

  def findByUserId(id: Long): Future[Seq[Role]]

}
