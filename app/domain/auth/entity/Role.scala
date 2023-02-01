package domain.auth.entity

import common.Constant.superAdmin
import domain.auth.value_obj.Permission
import domain.BaseEntity
import play.api.libs.json.{Json, OFormat}

import java.time.LocalDateTime

final case class Role(
  id: Long,
  code: String,
  name: String,
  permissions: Seq[Permission] = Nil,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseEntity {

  def update(name: String, permissionIds: Seq[Long]): Role = {
    val role = this.copy(name = name, updateAt = LocalDateTime.now())
    role.copy(permissions = permissionIds.map(id => Permission.justId(id)))
  }

  def beSuperAdmin: Boolean = name == superAdmin

}

object Role {

  implicit val format: OFormat[Role] = Json.format[Role]
  def justId(id: Long): Role         = Role(id, "", "")
}
