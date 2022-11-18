package domain.auth.repository

import domain.AggregateRepository
import domain.auth.value_obj.Role

import scala.concurrent.Future

/** 权限仓储
 */
trait RoleAggregateRepository extends AggregateRepository[Role] {

  def getByUser(userId: Long): Future[Option[Role]]
}
