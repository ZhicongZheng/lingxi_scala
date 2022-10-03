package auth.domain

import scala.concurrent.Future

trait UserRepository extends BaseRepository[User]{

  def findByUsername(username: String): Future[Option[User]]

}
