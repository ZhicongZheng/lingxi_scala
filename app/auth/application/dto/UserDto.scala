package auth.application.dto

import auth.domain.{BaseInfo, User}
import play.api.data.format.Formats.localDateTimeFormat
import play.api.data.format.Formatter
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
) extends BaseInfo

object UserDto {

  implicit val dataFormat: Formatter[LocalDateTime] = localDateTimeFormat("yyyy-mm-dd HH:mm:ss")

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
