package auth.domain

import auth.domain.repository.BaseRepository

import scala.concurrent.Future

trait UserRepository extends BaseRepository[User] {

  def findByUsername(username: String): Future[Option[User]]

}
