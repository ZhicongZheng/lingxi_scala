package auth.controller

import auth.domain.{Permission, Role, User}
import auth.domain.repository.UserRepository
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, ControllerComponents, InjectedController, Request}

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.{Inject, Singleton}

@Singleton
class UserController @Inject()(override val controllerComponents: ControllerComponents,
                               val userRepository: UserRepository)
  extends InjectedController {

  val todoList: Seq[User] = Seq(
    User(1, "test1", "123456", "s", "dsasd"),
    User(2, "test2", "123456", "s", "dsasd"),
    User(3, "test3", "123456", "s", "dsasd")
    )

  def getAll = Action.async { implicit request: Request[AnyContent] =>
      userRepository.list().map((users: Seq[User]) => Ok(Json.toJson(users)))
  }

  def getById(userId: Long) = Action {
    val foundItem: Option[User] = todoList.find((_: User).id == userId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }

}
