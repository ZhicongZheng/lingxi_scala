package auth.repository.impl

import auth.domain.repository.RoleRepository
import auth.domain.{BaseInfo, Role}
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

  override def findById(id: Long): Future[Option[BaseInfo]] = ???

  override def list(): Future[Seq[BaseInfo]] = ???

  override def count(): Future[Int] = ???

  override def create(d: BaseInfo): Future[Long] = ???

  override def update(d: BaseInfo): Future[Int] = ???

  override def delete(id: Long): Future[Int] = ???
}
