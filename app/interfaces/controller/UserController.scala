package interfaces.controller

import akka.util.ByteString
import application.command.{ChangePasswordCommand, CreateUserCommand, LoginCommand, UpdateUserCommand}
import application.service.{UserCommandService, UserQueryService}
import common.{Constant, Kaptcha, LOGIC_CODE_ERR, Page, PageQuery, Results}
import infra.actions.{AuthenticationAction, AuthorizationAction}
import interfaces.dto.UserDto
import play.api.http.HttpEntity
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, ControllerComponents, InjectedController, ResponseHeader, Result, Session}

import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.imageio.ImageIO
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserController @Inject() (
  override val controllerComponents: ControllerComponents,
  userCommandService: UserCommandService,
  userQueryService: UserQueryService,
  authenticationAction: AuthenticationAction,
  authorizationAction: AuthorizationAction
) extends InjectedController {

  def login = Action(parse.json[LoginCommand]).async { request =>
    val loginRequest = request.body
    request.session.get(Constant.loginCode) match {
      case Some(code) if code == loginRequest.code =>
        request.session - Constant.loginCode
        userCommandService
          .login(loginRequest)
          .map {
            case Left(error) => Results.fail(error)
            case Right(user) => Ok.withSession(Session(Map(Constant.SESSION_USER -> Json.toJson(user).toString())))
          }
          .recover(ex => Results.fail(ex))

      case _ => Future.successful(Results.fail(LOGIC_CODE_ERR))
    }
  }

  def logout = authenticationAction async { request =>
    Future.successful(Ok.withSession(request.session - Constant.SESSION_USER))
  }

  def current = authenticationAction { implicit request =>
    Results.success(UserDto.fromDo(request.user))
  }

  def loginCode = Action {
    val code  = Kaptcha.createText
    val image = Kaptcha.createImage(code)
    val os    = new ByteArrayOutputStream()
    ImageIO.write(image, "png", os)
    val base64 = Base64.getEncoder.encode(os.toByteArray)
    Ok(base64).withSession(Session(Map(Constant.loginCode -> code)))
  }

  def listUserByPage(page: Int, size: Int, sort: Option[String] = None) =
    authenticationAction andThen authorizationAction async {
      implicit val userFormat: OFormat[Page[UserDto]] = Json.format[Page[UserDto]]
      val pageQuery                                   = PageQuery(page, size, sort)
      userQueryService.listUserByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
    }

  def deleteUser(id: Int): Action[AnyContent] = authenticationAction andThen authorizationAction async {
    userCommandService.deleteUser(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

  def createUser: Action[CreateUserCommand] =
    authenticationAction(parse.json[CreateUserCommand]) andThen authorizationAction async { request =>
      userCommandService
        .createUser(request.body)
        .map {
          case Left(error)   => Results.fail(error)
          case Right(userId) => Created(Json.toJson(userId))
        }
        .recover(ex => Results.fail(ex))
    }

  def updateUser: Action[UpdateUserCommand] =
    authenticationAction(parse.json[UpdateUserCommand]) andThen authorizationAction async { request =>
      userCommandService
        .updateUser(request.body.copy(updateBy = request.user.id))
        .map {
          case Left(error) => Results.fail(error)
          case Right(_)    => Ok
        }
        .recover(ex => Results.fail(ex))
    }

  def changePwd: Action[ChangePasswordCommand] =
    authenticationAction(parse.json[ChangePasswordCommand]) async { request =>
      userCommandService.changePwd(request.user.id, request.body) map {
        case Left(error) => Results.fail(error)
        case Right(_)    => Ok
      } recover (ex => Results.fail(ex))
    }
}
