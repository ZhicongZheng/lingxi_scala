package application.command

import play.api.libs.json.{Json, OFormat}

case class ChangePasswordCommand(oldPassword: String, newPassword: String)

object ChangePasswordCommand {
  implicit val format: OFormat[ChangePasswordCommand] = Json.format[ChangePasswordCommand]
}
