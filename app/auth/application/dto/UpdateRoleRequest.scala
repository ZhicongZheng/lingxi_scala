package auth.application.dto

import play.api.libs.json.Json

case class UpdateRoleRequest(id: Long, name: String, permissions: Seq[Long] = Nil, updateBy: Option[Long] = None)

object UpdateRoleRequest {
  implicit val format = Json.format[UpdateRoleRequest]
}
