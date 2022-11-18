package infra.db.repository

import common.{Page, PageQuery}
import domain.user.entity.User
import domain.user.repository.UserRepository
import infra.db.assembler.UserAssembler._
import infra.db.po.RolePo.UserRoleTable
import infra.db.po.UserPo.UserTable
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepositoryImpl @Inject() (private val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends UserRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig = dbConfigProvider.get[PostgresProfile]

  private val users = TableQuery[UserTable]

  private val userRoles = TableQuery[UserRoleTable]

  override def findById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption).map(toDoOpt)

  override def list(): Future[Seq[User]] =
    db.run(users.result).map(toDoSeq)

  override def findByUsername(username: String): Future[Option[User]] =
    db.run(users.filter(_.username === username).result.headOption).map(toDoOpt)

  override def create(user: User): Future[Long] =
    db.run((users returning users.map(_.id)) += user)

  override def update(user: User): Future[Int] =
    db.run(users.filter(_.id === user.id).update(user.copy(updateAt = LocalDateTime.now())))

  override def delete(id: Long): Future[Int] =
    db.run {
      for {
        delUserCount <- users.filter(_.id === id).delete
        _            <- userRoles.filter(_.userId === id).delete
      } yield delUserCount
    }

  override def count(): Future[Int] = db.run(users.size.result)

  override def listByPage(pageQuery: PageQuery): Future[Page[User]] =
    db.run {
      for {
        userPos <- users.drop(pageQuery.offset).take(pageQuery.limit).result
        count   <- users.length.result
      } yield Page(pageQuery.page, pageQuery.size, count, userPos.map(toDo))
    }

  override def changeRole(user: User): Future[Unit] = {
    val userRole = (user.id, user.role.get.id)
    db.run {
      for {
        delete <- userRoles.filter(t => t.userId === user.id).delete
        insert <- userRoles.map(t => (t.userId, t.roleId)) += userRole
      } yield ()
    }
  }
}
