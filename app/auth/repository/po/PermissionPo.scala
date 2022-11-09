package auth.repository.po

import auth.domain.{BaseInfo, Permission}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.time.LocalDateTime

final case class PermissionPo(
  id: Long,
  `type`: String,
  value: String,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BasePo[Permission] {

  implicit override def toDo: Permission = Permission(id, `type`, value, None, createBy, updateBy, createAt, updateAt)
}

object PermissionPo {

  class PermissionTable(tag: Tag) extends Table[PermissionPo](tag, "permissions") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def `type` = column[String]("type")

    def value = column[String]("value")

    def createBy = column[Long]("create_by")

    def updateBy = column[Long]("update_by")

    def createAt = column[LocalDateTime]("create_at")

    def updateAt = column[LocalDateTime]("update_at")

    override def * = (
      id,
      `type`,
      value,
      createBy,
      updateBy,
      createAt,
      updateAt
    ) <> ((PermissionPo.apply _).tupled, PermissionPo.unapply)
  }

  class RolePermissionTable(tag: Tag) extends Table[(Long, Long, Long)](tag, "role_permissions") {

    def id = column[Long]("id", O.PrimaryKey)

    def roleId = column[Long]("role_id")

    def permissionId = column[Long]("permission_id")

    override def * = (id, roleId, permissionId)
  }

}
