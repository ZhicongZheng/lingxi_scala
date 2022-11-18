package interfaces.controller

import application.command.{ChangePasswordCommand, CreateRoleCommand, CreateUserRequest, LoginCommand, UpdateRoleCommand}
import application.service.{AuthCommandService, UserCommandService}
import common.{Constant, LOGIC_CODE_ERR, Results}
import infra.actions.{AuthorizationAction, UserAction}
import infra.filters.AuthenticationFilter
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class AuthWriteController @Inject() (
  override val controllerComponents: ControllerComponents,
  authApplicationService: AuthCommandService,
  userCommandService: UserCommandService,
  authenticationFilter: AuthenticationFilter,
  authedAction: UserAction,
  authorizationAction: AuthorizationAction
) extends InjectedController {

  def login: Action[LoginCommand] = Action(parse.json[LoginCommand]).async { request =>
    val loginRequest = request.body
    request.session.get(Constant.loginCode) match {
      case Some(code) if code == loginRequest.code =>
        userCommandService
          .login(loginRequest)
          .map {
            case Left(error)  => Results.fail(error)
            case Right(token) => Ok.withHeaders((HeaderNames.AUTHORIZATION, token))
          }
          .recover(ex => Results.fail(ex))

      case _ => Future.successful(Results.fail(LOGIC_CODE_ERR))
    }
  }

  def logout: Action[AnyContent] = authedAction async { request =>
    request.request.headers.get(HeaderNames.AUTHORIZATION) match {
      case Some(jwtToken) => authenticationFilter.logout(jwtToken).map(_ => Ok)
      case None           => Future.successful(Ok)
    }
  }

  def deleteUser(id: Int): Action[AnyContent] = authedAction andThen authorizationAction async {
    userCommandService.deleteUser(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

  def createUser: Action[CreateUserRequest] = authedAction(parse.json[CreateUserRequest]) async { request =>
    userCommandService
      .createUser(request.body)
      .map {
        case Left(error)   => Results.fail(error)
        case Right(userId) => Created(Json.toJson(userId))
      }
      .recover(ex => Results.fail(ex))
  }

  def changePwd: Action[ChangePasswordCommand] = authedAction(parse.json[ChangePasswordCommand]) async { request =>
    userCommandService.changePwd(request.user.id, request.body) map {
      case Left(error) => Results.fail(error)
      case Right(_)    => Ok
    } recover (ex => Results.fail(ex))
  }

  def changeUserRole(userId: Long, roleId: Long): Action[AnyContent] = authedAction andThen authorizationAction async {
    authApplicationService.changeUserRole(userId, roleId) map {
      case Left(error) => Results.fail(error)
      case Right(_)    => Ok
    } recover (ex => Results.fail(ex))
  }

  def createRole: Action[CreateRoleCommand] = authedAction(parse.json[CreateRoleCommand]) andThen authorizationAction async { request =>
    authApplicationService
      .createRole(request.body)
      .map {
        case Left(error) => Results.fail(error)
        case Right(id)   => Created(Json.toJson(id))
      }
      .recover(ex => Results.fail(ex))
  }

  def updateRole: Action[UpdateRoleCommand] = authedAction(parse.json[UpdateRoleCommand]) andThen authorizationAction async { request =>
    val updater           = request.request.user.id
    val updateRoleRequest = request.body.copy(updateBy = Some(updater))
    authApplicationService
      .updateRole(updateRoleRequest)
      .map {
        case Left(error) => Results.fail(error)
        case Right(_)    => Ok
      }
      .recover(ex => Results.fail(ex))
  }

  def deleteRole(id: Int): Action[AnyContent] = authedAction andThen authorizationAction async {
    authApplicationService
      .deleteRole(id)
      .map {
        case Left(error) => Results.fail(error)
        case Right(_)    => Ok
      }
      .recover(ex => Results.fail(ex))
  }
}
