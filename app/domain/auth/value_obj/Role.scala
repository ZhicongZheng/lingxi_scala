package domain.auth.value_obj

import domain.BaseEntity
import interfaces.dto.UpdateRoleRequest

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

  def update(request: UpdateRoleRequest): Role = {
    val role = this.copy(name = request.name, updateAt = LocalDateTime.now())
    val permissions = request.permissions match {
      case Nil => Nil
      case ps  => ps.map(id => Permission(id, "", ""))
    }
    role.copy(permissions = permissions)
  }

}
