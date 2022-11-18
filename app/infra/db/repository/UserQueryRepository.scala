package infra.db.repository

import infra.db.po.UserPo

import scala.concurrent.Future

trait UserQueryRepository extends QueryRepository[UserPo] {

  def findByUsername(username: String): Future[Option[UserPo]]
}
