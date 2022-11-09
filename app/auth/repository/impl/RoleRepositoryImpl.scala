package auth.repository.impl

import auth.domain.repository.RoleRepository
import auth.domain.Role
import auth.repository.po.PermissionPo.RolePermissionTable
import auth.repository.po.RolePo.{RoleTable, UserRoleTable}
import common.{PageDto, PageQuery}
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

  private val rolePermissions = TableQuery[RolePermissionTable]

  override def findByUserId(userId: Long): Future[Option[Role]] = {
    val joinQuery = roles join userRoles on (_.id === _.roleId)
    db.run(joinQuery.filter(_._2.userId === userId).map(_._1).result.headOption).map(roleOpt => roleOpt.map(po => po.toDo))
  }

  override def findById(id: Long): Future[Option[Role]] =
    db.run(roles.filter(_.id === id).result.headOption).map(roleOpt => roleOpt.map(r => r.toDo))

  override def list(): Future[Seq[Role]] = db.run(roles.result).map(roles => roles.map(r => r.toDo))

  override def count(): Future[Int] = db.run(roles.size.result)

  override def create(role: Role): Future[Long] = db.run(roles returning roles.map(_.id) += role)

  override def update(role: Role): Future[Int] = {
    val updatePermissions = role.permissions.map(p => (role.id, p.id))
    db.run {
      for {
        update <- roles.filter(_.id === role.id).update(role)
        del    <- rolePermissions.filter(t => t.roleId === role.id).delete
        insert <- rolePermissions.map(t => (t.roleId, t.permissionId)) ++= updatePermissions
      } yield update
    }
  }

  override def delete(id: Long): Future[Int] =
    db.run {
      for {
        delCount <- roles.filter(_.id === id).delete
        _        <- rolePermissions.filter(_.roleId === id).delete
      } yield delCount
    }

  override def listByPage(pageQuery: PageQuery): Future[PageDto[Role]] =
    db.run {
      for {
        rolePos <- roles.drop(pageQuery.offset).take(pageQuery.limit).result
        count   <- roles.length.result
      } yield PageDto(pageQuery.page, pageQuery.size, count, rolePos.map(_.toDo))
    }
}
