package interfaces.dto

import domain.auth.value_obj.Role
import play.api.libs.json.Json

import scala.language.implicitConversions

case class CreateRoleRequest(code: String, name: String)
object CreateRoleRequest {
  implicit val format = Json.format[CreateRoleRequest]

  implicit def requestToDo(request: CreateRoleRequest): Role = Role(-1, request.code, request.name)
}
