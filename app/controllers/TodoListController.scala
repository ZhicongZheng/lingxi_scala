package controllers

import domain.TodoListItem
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, ControllerComponents, InjectedController}

import javax.inject.Inject

class TodoListController @Inject() (override val controllerComponents: ControllerComponents) extends InjectedController {

  val todoList: Seq[TodoListItem] = Seq(
     TodoListItem(1, "test", isItDone = true),
     TodoListItem(2, "some other value", isItDone = false))

  implicit val todoListJson: OFormat[TodoListItem] = Json.format[TodoListItem]

  def getAll: Action[AnyContent] = Action {
    if (todoList.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(todoList))
    }
  }

  def getById(itemId: Long): Action[AnyContent] = Action {
    val foundItem: Option[TodoListItem] = todoList.find((_: TodoListItem).id == itemId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }

}
