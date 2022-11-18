package domain.user.repository

import domain.AggregateRepository
import domain.user.entity.User

import scala.concurrent.Future

trait UserRepository extends AggregateRepository[User] {

  def getByName(username: String): Future[Option[User]]
}
