package auth.repository.impl

import auth.domain.Permission
import auth.domain.repository.PermissionRepository
import auth.repository.po.PermissionPo.{PermissionTable, RolePermissionTable}
import common.{PageDto, PageQuery}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PermissionRepositoryImpl @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends PermissionRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig = dbConfigProvider.get[PostgresProfile]

  private val permissions = TableQuery[PermissionTable]

  private val rolePermission = TableQuery[RolePermissionTable]

  override def findByRoleId(roleId: Long): Future[Seq[Permission]] = {
    val joinQuery = permissions join rolePermission on (_.id === _.permissionId) filter (_._2.roleId === roleId)
    db.run(joinQuery.map(_._1).result).map(permissions => permissions.map(po => po.toDo))
  }

  override def findById(id: Long): Future[Option[Permission]] = ???

  override def list(): Future[Seq[Permission]] = ???

  override def count(): Future[Int] = ???

  override def create(d: Permission): Future[Long] = ???

  override def update(d: Permission): Future[Int] = ???

  override def delete(id: Long): Future[Int] = ???

  override def listByPage(pageQuery: PageQuery): Future[PageDto[Permission]] = ???
}
