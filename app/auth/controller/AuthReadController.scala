package auth.controller

import auth.application.AuthApplicationService
import auth.application.dto.UserDto
import common.Results
import common.actions.{ UserAction, UserRequest }
import play.api.mvc.{ Action, AnyContent, ControllerComponents, InjectedController }

import javax.inject.{ Inject, Singleton }

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

}
