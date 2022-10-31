package auth.application.dto

import play.api.libs.json.Json

case class LoginRequest(username: String, password: String, code: String) {}

object LoginRequest {

  implicit val format = Json.format[LoginRequest]
}
