package auth.controller

import auth.application.AuthApplicationService
import auth.application.dto.UserDto
import common.actions.{ UserAction, UserRequest }
import common.{ PageQuery, Results }
import play.api.mvc.{ Action, AnyContent, ControllerComponents, InjectedController }

import javax.inject.{ Inject, Singleton }
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuthReadController @Inject() (
  override val controllerComponents: ControllerComponents,
  authApplicationService: AuthApplicationService,
  authedAction: UserAction
) extends InjectedController {

  def current: Action[AnyContent] = authedAction { implicit request: UserRequest[AnyContent] =>
    val currentUser = UserDto.fromDo(request.user)
    Results.success(currentUser)
  }

  def listByPage(page: Int, size: Int, sort: Option[String] = None): Action[AnyContent] = authedAction async {
    val pageQuery = PageQuery(page, size, sort)
    authApplicationService.listByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
  }

}
