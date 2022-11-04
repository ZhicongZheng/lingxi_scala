package auth.controller

import auth.application.AuthApplicationService
import auth.application.dto.{ChangePasswordRequest, CreateRoleRequest, CreateUserRequest, LoginRequest}
import common.{Constant, Results}
import common.actions.{AuthorizationAction, UserAction}
import common.filters.AuthenticationFilter
import common.result.LOGIC_CODE_ERR
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
  authedAction: UserAction,
  authorizationAction: AuthorizationAction
) extends InjectedController {

  def login = Action(parse.json[LoginRequest]).async { request =>
    val loginRequest = request.body
    request.session.get(Constant.loginCode) match {
      case Some(code) if code == loginRequest.code =>
        authApplicationService
          .login(loginRequest)
          .map {
            case Left(error)  => Results.fail(error)
            case Right(token) => Ok.withHeaders((HeaderNames.AUTHORIZATION, token))
          }
          .recover(ex => Results.fail(ex))

      case _ => Future.successful(Results.fail(LOGIC_CODE_ERR))
    }
  }

  def logout = authedAction async { request =>
    request.request.headers.get(HeaderNames.AUTHORIZATION) match {
      case Some(jwtToken) => authenticationFilter.logout(jwtToken).map(_ => Ok)
      case None           => Future.successful(Ok)
    }
  }

  def deleteUser(id: Int) = authedAction andThen authorizationAction async {
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

  def createRole = authedAction(parse.json[CreateRoleRequest]) andThen authorizationAction async { request =>
    authApplicationService
      .createRole(request.body)
      .map(roleId => Created(Json.toJson(roleId)))
      .recover(ex => Results.fail(ex))
  }

  def deleteRole(id: Int) = authedAction andThen authorizationAction async {
    authApplicationService
      .deleteRole(id)
      .map {
        case Left(error)  => Results.fail(error)
        case Right(value) => Results.success(value)
      }
      .recover(ex => Results.fail(ex))
  }
}
