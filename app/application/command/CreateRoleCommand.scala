package application.command

import domain.auth.value_obj.Role
import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class CreateRoleCommand(code: String, name: String)
object CreateRoleCommand {
  implicit val format: OFormat[CreateRoleCommand] = Json.format[CreateRoleCommand]

  implicit def requestToDo(request: CreateRoleCommand): Role = Role(-1, request.code, request.name)
}
