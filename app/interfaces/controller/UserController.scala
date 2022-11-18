package interfaces.controller

import application.command.{ChangePasswordCommand, CreateUserRequest, LoginCommand}
import application.service.{UserCommandService, UserQueryService}
import common.{Constant, LOGIC_CODE_ERR, Page, PageQuery, Results}
import domain.user.entity.User
import infra.actions.{AuthorizationAction, UserAction}
import infra.filters.AuthenticationFilter
import interfaces.dto.UserDto
import play.api.http.HeaderNames
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, ControllerComponents, InjectedController, Session}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserController @Inject() (
  override val controllerComponents: ControllerComponents,
  userCommandService: UserCommandService,
  userQueryService: UserQueryService,
  userAction: UserAction,
  authorizationAction: AuthorizationAction,
  authenticationFilter: AuthenticationFilter
) extends InjectedController {

  def login = Action(parse.json[LoginCommand]).async { request =>
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

  def logout = userAction async { request =>
    request.request.headers.get(HeaderNames.AUTHORIZATION) match {
      case Some(jwtToken) => authenticationFilter.logout(jwtToken).map(_ => Ok)
      case None           => Future.successful(Ok)
    }
  }

  def current = userAction { implicit request =>
    Results.success(UserDto.fromDo(request.user))
  }

  def loginCode = Action { implicit request =>
    val code = User.loginCode
    Ok(code).withSession(Session(Map(Constant.loginCode -> code)))
  }

  def listUserByPage(page: Int, size: Int, sort: Option[String] = None) = userAction andThen authorizationAction async {
    implicit val userFormat: OFormat[Page[UserDto]] = Json.format[Page[UserDto]]
    val pageQuery                                   = PageQuery(page, size, sort)
    userQueryService.listUserByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
  }

  def deleteUser(id: Int): Action[AnyContent] = userAction andThen authorizationAction async {
    userCommandService.deleteUser(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

  def createUser: Action[CreateUserRequest] = userAction(parse.json[CreateUserRequest]) async { request =>
    userCommandService
      .createUser(request.body)
      .map {
        case Left(error)   => Results.fail(error)
        case Right(userId) => Created(Json.toJson(userId))
      }
      .recover(ex => Results.fail(ex))
  }

  def changePwd: Action[ChangePasswordCommand] = userAction(parse.json[ChangePasswordCommand]) async { request =>
    userCommandService.changePwd(request.user.id, request.body) map {
      case Left(error) => Results.fail(error)
      case Right(_)    => Ok
    } recover (ex => Results.fail(ex))
  }
}
