package auth.domain.repository

import auth.domain.User
import common.{ PageDto, PageQuery }

import scala.concurrent.Future

trait UserRepository extends BaseRepository[User] {

  def findByUsername(username: String): Future[Option[User]]

  def listByPage(pageQuery: PageQuery): Future[PageDto[User]]

}
