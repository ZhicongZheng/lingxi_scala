package domain.auth.repository

import domain.BaseRepository
import domain.auth.value_obj.Role

import scala.concurrent.Future

trait RoleRepository extends BaseRepository[Role] {

  def findByUserId(userId: Long): Future[Option[Role]]

  def findByCode(code: String): Future[Option[Role]]

}
