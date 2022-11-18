package domain.auth.repository

import domain.AggregateRepository
import domain.auth.entity.Role

import scala.concurrent.Future

/** 权限仓储
 */
trait RoleRepository extends AggregateRepository[Role] {

  def getByUser(userId: Long): Future[Option[Role]]
}
