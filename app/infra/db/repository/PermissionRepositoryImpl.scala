package infra.db.repository

import infra.db.po.PermissionPo.{PermissionTable, RolePermissionTable}
import common.{Page, PageQuery}
import domain.auth.repository.PermissionRepository
import domain.auth.value_obj.Permission
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery
import infra.db.assembler.AuthorityAssembler._

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
    db.run(joinQuery.map(_._1).result).map(toPermissionDoSeq)
  }

  override def findById(id: Long): Future[Option[Permission]] = ???

  override def list(): Future[Seq[Permission]] = ???

  override def count(): Future[Int] = ???

  override def create(d: Permission): Future[Long] = ???

  override def update(d: Permission): Future[Int] = ???

  override def delete(id: Long): Future[Int] = ???

  override def listByPage(pageQuery: PageQuery): Future[Page[Permission]] = ???
}
