package interfaces.controller

import application.command.{CreateRoleCommand, UpdateRoleCommand}
import application.service.{RoleCommandService, RoleQueryService, UserQueryService}
import common.{Page, PageQuery, Results}
import infra.actions.{AuthorizationAction, UserAction}
import interfaces.dto.{PermissionDto, RoleDto}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{ControllerComponents, InjectedController}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class RoleController @Inject() (
  override val controllerComponents: ControllerComponents,
  roleQueryService: RoleQueryService,
  roleCommandService: RoleCommandService,
  userAction: UserAction,
  authorizationAction: AuthorizationAction
) extends InjectedController {

  def listRoleByPage(page: Int, size: Int, sort: Option[String] = None) = userAction andThen authorizationAction async {
    implicit val roleFormat: OFormat[Page[RoleDto]] = Json.format[Page[RoleDto]]
    val pageQuery                                   = PageQuery(page, size, sort)
    roleQueryService.listRolesByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
  }

  def changeUserRole(userId: Long, roleId: Long) = userAction andThen authorizationAction async {
    roleCommandService.changeUserRole(userId, roleId) map {
      case Left(error) => Results.fail(error)
      case Right(_)    => Ok
    } recover (ex => Results.fail(ex))
  }

  def createRole = userAction(parse.json[CreateRoleCommand]) andThen authorizationAction async { request =>
    roleCommandService
      .createRole(request.body)
      .map {
        case Left(error) => Results.fail(error)
        case Right(id)   => Created(Json.toJson(id))
      }
      .recover(ex => Results.fail(ex))
  }

  def updateRole = userAction(parse.json[UpdateRoleCommand]) andThen authorizationAction async { request =>
    val updater           = request.request.user.id
    val updateRoleRequest = request.body.copy(updateBy = Some(updater))
    roleCommandService
      .updateRole(updateRoleRequest)
      .map {
        case Left(error) => Results.fail(error)
        case Right(_)    => Ok
      }
      .recover(ex => Results.fail(ex))
  }

  def deleteRole(id: Int) = userAction andThen authorizationAction async {
    roleCommandService
      .deleteRole(id)
      .map {
        case Left(error) => Results.fail(error)
        case Right(_)    => Ok
      }
      .recover(ex => Results.fail(ex))
  }

  def listPermission() = userAction andThen authorizationAction async {
    roleQueryService
      .listPermission()
      .map(ps => ps.map(PermissionDto.fromPo))
      .map(permissions => Results.success(permissions))
      .recover(ex => Results.fail(ex))
  }
}
