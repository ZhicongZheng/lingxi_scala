package infra.db.repository.impl

import common.{Page, PageQuery}
import infra.db.po.{PermissionPo, RolePo}
import infra.db.po.PermissionPo.PermissionTable
import infra.db.po.RolePo.{RoleTable, UserRoleTable}
import infra.db.repository.RoleQueryRepository
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RoleQueryRepositoryImpl @Inject() (private val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends RoleQueryRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig = dbConfigProvider.get[PostgresProfile]

  private val roles = TableQuery[RoleTable]

  private val permissions = TableQuery[PermissionTable]

  private val userRoles = TableQuery[UserRoleTable]

  override def get(id: Long): Future[Option[RolePo]] = db.run(roles.filter(_.id === id).result.headOption)

  override def list(): Future[Seq[RolePo]] = db.run(roles.result)

  override def count(): Future[Int] = db.run(roles.size.result)

  override def listByPage(pageQuery: PageQuery): Future[Page[RolePo]] = db.run {
    for {
      rolePos <- roles.drop(pageQuery.offset).take(pageQuery.limit).result
      count   <- roles.length.result
    } yield Page(pageQuery.page, pageQuery.size, count, rolePos)
  }

  override def findByCode(code: String): Future[Option[RolePo]] = db.run(roles.filter(_.code === code).result.headOption)

  override def listPermissions(): Future[Seq[PermissionPo]] = db.run(permissions.result)

  override def findUserRoleMap(userId: Seq[Long]): Future[Map[Long, RolePo]] =
    db.run(userRoles.filter(_.userId inSet userId).result).flatMap { userRoles =>
      val roleUserMap = userRoles.map(ur => (ur._3 -> ur._2)).toMap

      db.run(roles.filter(_.id inSet roleUserMap.keys).result).map { roles =>
        roles.map(role => (roleUserMap(role.id) -> role)).toMap
      }
    }

}
