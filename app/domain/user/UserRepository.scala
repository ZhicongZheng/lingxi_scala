package domain.user

import domain.AggregateRepository

import scala.concurrent.Future

trait UserRepository extends AggregateRepository[User] {

  def getByName(username: String): Future[Option[User]]
}
