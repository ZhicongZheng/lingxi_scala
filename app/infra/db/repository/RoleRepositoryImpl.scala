package infra.db.repository

import infra.db.po.PermissionPo.RolePermissionTable
import infra.db.po.RolePo.{RoleTable, UserRoleTable}
import common.{Page, PageQuery}
import domain.auth.repository.RoleRepository
import domain.auth.value_obj.Role
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery
import infra.db.assembler.AuthorityAssembler._

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
    db.run(joinQuery.filter(_._2.userId === userId).map(_._1).result.headOption).map(toRoleDoOpt)
  }

  override def findById(id: Long): Future[Option[Role]] =
    db.run(roles.filter(_.id === id).result.headOption).map(toRoleDoOpt)

  override def list(): Future[Seq[Role]] = db.run(roles.result).map(toRoleDoSeq)

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

  override def listByPage(pageQuery: PageQuery): Future[Page[Role]] =
    db.run {
      for {
        rolePos <- roles.drop(pageQuery.offset).take(pageQuery.limit).result
        count   <- roles.length.result
      } yield Page(pageQuery.page, pageQuery.size, count, rolePos.map(toDo))
    }

  override def findByCode(code: String): Future[Option[Role]] =
    db.run(roles.filter(_.code === code).result.headOption).map(roleOpt => roleOpt.map(toDo))
}
