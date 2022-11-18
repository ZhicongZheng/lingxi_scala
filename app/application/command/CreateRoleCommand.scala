package application.command

import common.Constant
import domain.auth.entity.Role
import domain.auth.value_obj.Permission
import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class CreateRoleCommand(code: String, name: String, permission: Seq[Long] = Nil)
object CreateRoleCommand {
  implicit val format: OFormat[CreateRoleCommand] = Json.format[CreateRoleCommand]

  implicit def requestToDo(request: CreateRoleCommand): Role =
    Role(Constant.domainCreateId, request.code, request.name, permissions = request.permission.map(id => Permission(id, "", "")))
}
