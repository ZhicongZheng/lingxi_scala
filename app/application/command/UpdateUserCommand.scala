package application.command

import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class UpdateUserCommand(
  id: Long,
  avatar: String,
  nickName: String,
  email: String,
  phone: Option[String] = None,
  role: Long,
  updateBy: Long = -1
)

object UpdateUserCommand {

  implicit val format: OFormat[UpdateUserCommand] = Json.format[UpdateUserCommand]

  implicit def commandToDo(command: UpdateUserCommand): (String, String, String, String, Long, Long) =
    (
      command.avatar,
      command.nickName,
      command.phone.getOrElse(""),
      command.email,
      command.role,
      command.updateBy
    )
}
