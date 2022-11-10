package domain.user.repository

import domain.user.entity.User
import domain.BaseRepository

import scala.concurrent.Future

trait UserRepository extends BaseRepository[User] {

  def findByUsername(username: String): Future[Option[User]]

  def changeRole(user: User): Future[Unit]

}
