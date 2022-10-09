package auth.controller

import auth.application.AuthApplicationService
import auth.application.dto.UserDto
import common.ResultHelper
import common.actions.{UserAction, UserRequest}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents, InjectedController}

import javax.inject.{Inject, Singleton}

@Singleton
class AuthReadController @Inject()(override val controllerComponents: ControllerComponents,
                                   authApplicationService: AuthApplicationService,
                                   authedAction: UserAction) extends InjectedController {

  def currentUserInfo: Action[AnyContent] = authedAction { implicit request: UserRequest[AnyContent] =>
    val currentUser = UserDto.fromDo(request.user)
    Ok(ResultHelper.success(Json.toJson(currentUser)))
  }

}
