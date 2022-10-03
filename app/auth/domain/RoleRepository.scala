package auth.domain

import scala.concurrent.Future

trait RoleRepository extends BaseRepository[BaseInfo] {

  def findByUserId(id: Long): Future[Seq[Role]]

}
