package domain.auth.entity

import common.Constant.superAdmin
import domain.auth.value_obj.Permission
import domain.BaseEntity

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
    role.copy(permissions = permissionIds.map(id => Permission.just(id)))
  }

  def beSuperAdmin: Boolean = name == superAdmin

}
