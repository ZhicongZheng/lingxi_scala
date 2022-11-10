package interfaces.dto

import domain.BaseEntity
import domain.user.entity.User
import play.api.libs.json.{Json, OFormat}

import java.time.LocalDateTime
import scala.language.implicitConversions

case class UserDto(
  id: Long,
  username: String,
  password: String,
  avatar: String,
  nickName: String,
  phone: String,
  email: String,
  role: Option[RoleDto] = None,
  permissions: Seq[PermissionDto] = Nil,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseEntity

object UserDto {

  implicit val format: OFormat[UserDto] = Json.format[UserDto]

  implicit def fromDo(user: User): UserDto =
    UserDto(
      user.id,
      user.username,
      "",
      user.avatar,
      user.nickName,
      user.phone,
      user.email,
      user.role,
      user.permissions,
      user.createBy,
      user.updateBy,
      user.createAt,
      user.updateAt
    )
}
