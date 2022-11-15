package interfaces.dto

import play.api.libs.json.{Json, OFormat}

case class UpdateRoleCommand(id: Long, name: String, permissions: Seq[Long] = Nil, updateBy: Option[Long] = None)

object UpdateRoleCommand {
  implicit val format: OFormat[UpdateRoleCommand] = Json.format[UpdateRoleCommand]
}
