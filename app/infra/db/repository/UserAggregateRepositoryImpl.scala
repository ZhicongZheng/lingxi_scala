package infra.db.repository

import domain.auth.repository.RoleAggregateRepository
import domain.user.entity.User
import domain.user.repository.UserAggregateRepository
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
class UserAggregateRepositoryImpl @Inject() (
  private val dbConfigProvider: DatabaseConfigProvider,
  private val roleAggregateRepository: RoleAggregateRepository
)(implicit ec: ExecutionContext)
    extends UserAggregateRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig = dbConfigProvider.get[PostgresProfile]

  private val users = TableQuery[UserTable]

  private val userRoles = TableQuery[UserRoleTable]

  override def save(domain: User): Future[Long] =
    domain.id match {
      case 0 => doInsert(domain)
      case _ => doUpdate(domain)
    }

  override def get(id: Long): Future[Option[User]] =
    for {
      user <- db.run(users.filter(_.id === id).result.headOption).map(toDoOpt)
      role <- roleAggregateRepository.getByUser(id)
    } yield user.map(user => user.copy(role = role))

  override def remove(id: Long): Future[Unit] = db.run {
    for {
      del <- users.filter(_.id === id).delete
      _   <- userRoles.filter(_.userId === id).delete
    } yield ()
  }

  private def insertUserRole(userId: Long, roleId: Long): Future[Int] = db.run(userRoles.map(t => (t.userId, t.roleId)) += (userId, roleId))

  private def deleteUserRole(userId: Long): Future[Unit] = db.run(userRoles.filter(_.userId === userId).delete.map(_ => ()))

  private def doInsert(user: User): Future[Long] = {
    val insertUser = db.run((users returning users.map(_.id)) += user)

    insertUser.flatMap { userId =>
      user.role match {
        case None       => Future.successful(userId)
        case Some(role) => insertUserRole(userId, role.id).map(_ => userId)
      }
    }
  }

  private def doUpdate(user: User): Future[Long] = {
    val updateUser = db.run(users.filter(_.id === user.id).update(user.copy(updateAt = LocalDateTime.now())))

    updateUser.flatMap { _ =>
      user.role match {
        case None => Future.successful(user.id)
        case Some(role) =>
          deleteUserRole(user.id).flatMap(_ => insertUserRole(user.id, role.id).map(_ => user.id))
      }
    }
  }

}
