package domain.auth.value_obj

import common.Constant.superAdmin
import domain.BaseEntity
import interfaces.dto.UpdateRoleCommand

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
    val permissions = permissionIds match {
      case Nil => Nil
      case ps => ps.map(id => Permission(id, "", ""))
    }
    role.copy(permissions = permissions)
  }

  def beSuperAdmin: Boolean = name == superAdmin


}
