package application.command

import domain.user.entity.User
import User.entryPwd
import common.Constant
import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class CreateUserRequest(
  username: String,
  password: String,
  avatar: String,
  nickName: String,
  phone: Option[String] = None,
  email: String
)
object CreateUserRequest {

  implicit val format: OFormat[CreateUserRequest] = Json.format[CreateUserRequest]

  implicit def requestToDo(request: CreateUserRequest): User =
    User(
      Constant.domainCreateId,
      request.username,
      entryPwd(request.password),
      request.avatar,
      request.nickName,
      request.phone.getOrElse(""),
      request.email
    )
}
