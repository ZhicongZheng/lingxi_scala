package domain.auth.repository

import domain.auth.entity.Role
import domain.BaseRepository

import scala.concurrent.Future

trait RoleRepository extends BaseRepository[Role] {

  def findByUserId(userId: Long): Future[Option[Role]]

  def findByCode(code: String): Future[Option[Role]]

}
