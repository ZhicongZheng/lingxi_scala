package auth.controller

import auth.application.AuthApplicationService
import auth.application.dto.{ChangePasswordRequest, CreateUserRequest, LoginRequest}
import common.Results
import common.actions.UserAction
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuthWriteController @Inject() (
  override val controllerComponents: ControllerComponents,
  authApplicationService: AuthApplicationService,
  authedAction: UserAction
) extends InjectedController {

  def login = Action(parse.json[LoginRequest]).async { request =>
    authApplicationService
      .login(request.body)
      .map {
        case Left(error)  => Results.fail(error)
        case Right(token) => Ok.withHeaders((HeaderNames.AUTHORIZATION, token))
      }
      .recover(ex => Results.fail(ex))
  }

  def deleteUser(id: Int) = authedAction async {
    authApplicationService.deleteUser(id).map(c => Results.success(c)).recover(ex => Results.fail(ex))
  }

  def createUser = authedAction(parse.json[CreateUserRequest]) async { request =>
    authApplicationService
      .createUser(request.body)
      .map {
        case Left(error)   => Results.fail(error)
        case Right(userId) => Created(Json.toJson(userId))
      }
      .recover(ex => Results.fail(ex))
  }

  def changePwd = authedAction(parse.json[ChangePasswordRequest]) async { request =>
    authApplicationService.changePwd(request.user.id, request.body) map {
      case Left(error) => Results.fail(error)
      case Right(_)    => Ok
    }
  }
}
