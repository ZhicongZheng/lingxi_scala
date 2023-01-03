package interfaces.api.endpoints

import application.command.{CreateRoleCommand, UpdateRoleCommand}
import common.Page
import interfaces.dto.{PermissionDto, RoleDto}
import play.api.libs.json.{Json, OFormat}
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.play.jsonBody

object RoleEndpoints {

  private val baseSecuredUserEndpoint = securedWithBearerEndpoint.in("roles").tag("Roles")

  implicit val roleFormat: OFormat[Page[RoleDto]] = Json.format[Page[RoleDto]]

  def endpoints = Seq(createRoleEndpoint, deleteRoleEndpoint, listByPageEndpoint, updateRoleEndpoint, listPermissionEndpoint)

  val createRoleEndpoint = baseSecuredUserEndpoint.post
    .name("createRole")
    .summary("创建角色")
    .description("创建自定义角色")
    .in(jsonBody[CreateRoleCommand])
    .out(statusCode(StatusCode.Created))
    .out(jsonBody[Long])

  val deleteRoleEndpoint = baseSecuredUserEndpoint.delete
    .name("deleteRole")
    .summary("删除角色")
    .description("删除角色，超级管理员角色不允许删除")
    .in(path[Int]("id"))
    .out(statusCode(StatusCode.Ok))

  val listByPageEndpoint = baseSecuredUserEndpoint.get
    .name("listRoleByPage")
    .summary("分页获取角色")
    .description("分页的方式获取角色列表，支持排序")
    .in(query[Int]("page").default(1) / query[Int]("size").default(10) / query[Option[String]]("sort"))
    .out(jsonBody[Page[RoleDto]])

  val updateRoleEndpoint = baseSecuredUserEndpoint.put
    .name("updateRole")
    .summary("更新角色")
    .description("更新角色信息，可以同时保存角色所分配的权限")
    .in(jsonBody[UpdateRoleCommand])
    .out(statusCode(StatusCode.Ok))

  val listPermissionEndpoint = baseSecuredUserEndpoint.get
    .name("listPermission")
    .summary("获取权限列表")
    .description("获取权限列表")
    .in("permissions")
    .out(statusCode(StatusCode.Ok))
    .out(jsonBody[Seq[PermissionDto]])
}
