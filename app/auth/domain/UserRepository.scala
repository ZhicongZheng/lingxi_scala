package auth.domain

import scala.concurrent.Future

trait UserRepository {

  def findById(id: Long): Future[User]

  def list(): Future[Seq[User]]

  def findByUsername(username: String): Future[Option[User]]

  def create(user: User): Future[User]

  def update(user: User): Future[User]

  def delete(id: Long): Future[User]

}
