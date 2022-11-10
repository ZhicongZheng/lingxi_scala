package interfaces.dto

import play.api.libs.json.Json

case class ChangePasswordRequest(oldPassword: String, newPassword: String)

object ChangePasswordRequest {
  implicit val format = Json.format[ChangePasswordRequest]
}
