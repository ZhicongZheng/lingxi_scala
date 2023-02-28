package interfaces.dto

import domain.user.User
import infra.db.po.UserPo
import play.api.libs.json.{Json, OFormat}
import io.scalaland.chimney.dsl._

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
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
)

object UserDto {

  implicit val format: OFormat[UserDto] = Json.format[UserDto]

  implicit def fromDo(user: User): UserDto =
    user.into[UserDto].withFieldConst(_.password, "").transform

  implicit def fromPo(user: UserPo): UserDto =
    user
      .into[UserDto]
      .withFieldConst(_.password, "")
      .withFieldConst(_.role, None)
      .transform
}
