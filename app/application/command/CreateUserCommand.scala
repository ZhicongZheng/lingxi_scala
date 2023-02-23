package application.command

import domain.user.User.entryPwd
import common.Constant
import domain.auth.Role
import domain.user.User
import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class CreateUserCommand(
  username: String,
  password: String,
  avatar: String,
  nickName: String,
  phone: Option[String] = None,
  email: String,
  role: Long
)
object CreateUserCommand {

  implicit val format: OFormat[CreateUserCommand] = Json.format[CreateUserCommand]

  implicit def requestToDo(command: CreateUserCommand): User =
    User(
      Constant.domainCreateId,
      command.username,
      entryPwd(command.password),
      command.avatar,
      command.nickName,
      command.phone.getOrElse(""),
      command.email,
      Some(Role.justId(command.role))
    )
}
