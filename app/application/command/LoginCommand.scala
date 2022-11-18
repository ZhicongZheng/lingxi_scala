package application.command

import play.api.libs.json.{Json, OFormat}

case class LoginCommand(username: String, password: String, code: String) {}

object LoginCommand {

  implicit val format: OFormat[LoginCommand] = Json.format[LoginCommand]
}
