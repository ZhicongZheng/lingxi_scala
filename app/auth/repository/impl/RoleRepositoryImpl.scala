package auth.repository.impl

import auth.domain.repository.RoleRepository
import auth.domain.Role
import auth.repository.po.RolePo.{RoleTable, UserRoleTable}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RoleRepositoryImpl @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends RoleRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig = dbConfigProvider.get[PostgresProfile]

  private val roles = TableQuery[RoleTable]

  private val userRoles = TableQuery[UserRoleTable]

  override def findByUserId(userId: Long): Future[Option[Role]] = {
    val joinQuery = roles join userRoles on (_.id === _.roleId)
    db.run(joinQuery.filter(_._2.userId === userId).map(_._1).result.headOption).map(roleOpt => roleOpt.map(po => po.toDo))
  }

  override def findById(id: Long): Future[Option[Role]] =
    db.run(roles.filter(_.id === id).result.headOption).map(roleOpt => roleOpt.map(r => r.toDo))

  override def list(): Future[Seq[Role]] = db.run(roles.result).map(roles => roles.map(r => r.toDo))

  override def count(): Future[Int] = db.run(roles.size.result)

  override def create(role: Role): Future[Long] = db.run(roles returning roles.map(_.id) += role)

  override def update(role: Role): Future[Int] = ???

  override def delete(id: Long): Future[Int] = ???
}
