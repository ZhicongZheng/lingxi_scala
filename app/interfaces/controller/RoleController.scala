package interfaces.controller

import application.command.{CreateRoleCommand, UpdateRoleCommand}
import application.service.{RoleCommandService, RoleQueryService}
import common.{Page, PageQuery, Results}
import infra.actions.{AuthenticationAction, AuthorizationAction}
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
  authenticationAction: AuthenticationAction,
  authorizationAction: AuthorizationAction
) extends InjectedController {

  def listRoleByPage(page: Int, size: Int, sort: Option[String] = None) =
    authenticationAction andThen authorizationAction async {
      implicit val roleFormat: OFormat[Page[RoleDto]] = Json.format[Page[RoleDto]]
      val pageQuery                                   = PageQuery(page, size, sort)
      roleQueryService.listRolesByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
    }

  def changeUserRole(userId: Long, roleId: Long) =
    authenticationAction andThen authorizationAction async {
      roleCommandService.changeUserRole(userId, roleId) map {
        case Left(error) => Results.fail(error)
        case Right(_)    => Ok
      } recover (ex => Results.fail(ex))
    }

  def createRole =
    authenticationAction(parse.json[CreateRoleCommand]) andThen authorizationAction async { request =>
      roleCommandService
        .createRole(request.body)
        .map {
          case Left(error) => Results.fail(error)
          case Right(id)   => Created(Json.toJson(id))
        }
        .recover(ex => Results.fail(ex))
    }

  def updateRole =
    authenticationAction(parse.json[UpdateRoleCommand]) andThen authorizationAction async { request =>
      val updater           = request.user.id
      val updateRoleRequest = request.body.copy(updateBy = Some(updater))
      roleCommandService
        .updateRole(updateRoleRequest)
        .map {
          case Left(error) => Results.fail(error)
          case Right(_)    => Ok
        }
        .recover(ex => Results.fail(ex))
    }

  def deleteRole(id: Int) = authenticationAction andThen authorizationAction async {
    roleCommandService
      .deleteRole(id)
      .map {
        case Left(error) => Results.fail(error)
        case Right(_)    => Ok
      }
      .recover(ex => Results.fail(ex))
  }

  def listPermission() = authenticationAction andThen authorizationAction async {
    roleQueryService
      .listPermission()
      .map(ps => ps.map(PermissionDto.fromPo))
      .map(permissions => Results.success(permissions))
      .recover(ex => Results.fail(ex))
  }
}
