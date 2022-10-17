package auth.controller

import auth.application.AuthApplicationService
import auth.application.dto.UserDto
import common.{PageQuery, Results}
import common.actions.{AuthorizationAction, UserAction, UserRequest}
import play.api.mvc.{AnyContent, ControllerComponents, InjectedController}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.ArrayDeque

@Singleton
class AuthReadController @Inject() (
  override val controllerComponents: ControllerComponents,
  authApplicationService: AuthApplicationService,
  userAction: UserAction,
  authorizationAction: AuthorizationAction
) extends InjectedController {

  def current = userAction { implicit request: UserRequest[AnyContent] =>
    val currentUser = UserDto.fromDo(request.user)
    Results.success(currentUser)
  }

  def listByPage(page: Int, size: Int, sort: Option[String] = None) = userAction andThen authorizationAction async {
    val pageQuery = PageQuery(page, size, sort)
    authApplicationService.listByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
  }

}
