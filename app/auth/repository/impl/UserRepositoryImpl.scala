package auth.repository.impl

import auth.domain.{ User, UserRepository }
import auth.repository.po.PermissionPo.PermissionTable
import auth.repository.po.RolePo.{ RoleTable, UserRoleTable }
import auth.repository.po.UserPo
import auth.repository.po.UserPo.UserTable
import common.{ PageDto, PageQuery }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfig }
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class UserRepositoryImpl @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends UserRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig = dbConfigProvider.get[PostgresProfile]

  private val users = TableQuery[UserTable]

  private val roles = TableQuery[RoleTable]

  private val userRoles = TableQuery[UserRoleTable]

  private val permissions = TableQuery[PermissionTable]

  override def findById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption).map(userPoOpt => userPoOpt.map(po => po.toDo))

  override def list(): Future[Seq[User]] =
    db.run(users.result).map(users => users.map(u => u.toDo))

  override def findByUsername(username: String): Future[Option[User]] =
    db.run(users.filter(_.username === username).result.headOption).map(userPoOpt => userPoOpt.map(po => po.toDo))

  override def create(user: User): Future[Long] = {
    val po = UserPo.fromDo(user)
    db.run((users returning users.map(_.id)) += po)
  }

  override def update(user: User): Future[Int] =
    db.run(
      users
        .filter(_.id === user.id)
        .map(u => (u.username, u.updateAt))
        .update(user.username, user.updateAt)
    )

  override def delete(id: Long): Future[Int] =
    db.run {
      for {
        delUserCount <- users.filter(_.id === id).delete
        _            <- userRoles.filter(_.userId === id).delete
      } yield delUserCount
    }

  override def count(): Future[Int] = db.run(users.size.result)

  override def listByPage(pageQuery: PageQuery): Future[PageDto[User]] =
    db.run {
      for {
        userPos <- users.drop(pageQuery.offset).take(pageQuery.limit).result
        count   <- users.length.result
      } yield PageDto(pageQuery.page, pageQuery.size, count, userPos.map(_.toDo))
    }
}
