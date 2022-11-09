package auth.domain.repository

import auth.domain.User

import scala.concurrent.Future

trait UserRepository extends BaseRepository[User] {

  def findByUsername(username: String): Future[Option[User]]

  def changeRole(user: User): Future[Unit]

}
