package auth.application.dto

import auth.domain.User
import play.api.libs.json.Json

import scala.language.implicitConversions

case class CreateUserRequest (username: String,
                              password: String,
                              avatar: String,
                              nickName: String)
object CreateUserRequest {

  implicit val format = Json.format[CreateUserRequest]

  implicit def requestToDo(request: CreateUserRequest): User = User.create(request.username, request.password, request.avatar, request.nickName)
}
