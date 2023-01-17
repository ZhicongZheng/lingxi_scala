package infra.db.repository.impl

import domain.action.{Action, ActionRepository}

import scala.concurrent.Future

class ActionRepositoryImpl extends ActionRepository {
  override def save(domain: Action): Future[Long] = ???

  override def get(id: Long): Future[Option[Action]] = ???

  override def remove(id: Long): Future[Unit] = ???
}
