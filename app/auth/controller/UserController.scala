package auth.controller

import auth.application.dto.UserDto
import auth.domain.UserRepository
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, ControllerComponents, InjectedController, Request}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserController @Inject() (
  override val controllerComponents: ControllerComponents,
  val userRepository: UserRepository
) extends InjectedController
    with Logging {

  val todoList: Seq[UserDto] = Seq(
    UserDto(1, "test1", "123456", "s", "dsasd"),
    UserDto(2, "test2", "123456", "s", "dsasd"),
    UserDto(3, "test3", "123456", "s", "dsasd")
  )

  def getAll = Action.async { implicit request: Request[AnyContent] =>
    userRepository.list().map(userDos => userDos.map(u => UserDto.fromDo(u))).map(result => Ok(Json.toJson(result)))
  }

  def getById(userId: Long) = Action {
    val foundItem: Option[UserDto] = todoList.find((_: UserDto).id == userId)

    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None       => NotFound
    }
  }

}
