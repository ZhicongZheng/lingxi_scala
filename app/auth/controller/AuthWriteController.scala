package auth.controller

import auth.application.AuthApplicationService
import auth.application.dto.{ChangePasswordRequest, CreateUserRequest, LoginRequest}
import common.Results
import common.actions.UserAction
import common.filters.AuthenticationFilter
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class AuthWriteController @Inject() (
  override val controllerComponents: ControllerComponents,
  authApplicationService: AuthApplicationService,
  authenticationFilter: AuthenticationFilter,
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

  def logout = authedAction async { request =>
    request.request.headers.get(HeaderNames.AUTHORIZATION) match {
      case Some(jwtToken) => authenticationFilter.logout(jwtToken).map(_ => Ok)
      case None           => Future.successful(Ok)
    }
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
