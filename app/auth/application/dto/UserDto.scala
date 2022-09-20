package auth.application.dto

import auth.domain.{BaseInfo, User}
import play.api.libs.json.Json

import java.time.LocalDateTime

case class UserDto(id: Long,
                   username: String,
                   password: String,
                   avatar: String,
                   nickName: String,
                   createBy: Long = 0L,
                   updateBy: Long = 0L,
                   createAt: LocalDateTime = LocalDateTime.now(),
                   updateAt: Option[LocalDateTime] = None) extends BaseInfo

object UserDto {


  implicit val format = Json.format[UserDto]

  def fromDo(user: User): UserDto = {
    UserDto(user.id, user.username, user.password, user.avatar, user.nickName, user.createBy, user.updateBy, user.createAt, user.updateAt)
  }
}