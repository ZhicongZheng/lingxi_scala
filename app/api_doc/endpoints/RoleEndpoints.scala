package api_doc.endpoints

import sttp.tapir._
import _root_.auth.application.dto.{CreateRoleRequest, RoleDto}
import common.PageDto
import sttp.model.StatusCode
import sttp.tapir.json.play.jsonBody
import sttp.tapir.generic.auto.schemaForCaseClass

object RoleEndpoints {

  private val baseSecuredUserEndpoint = securedWithBearerEndpoint.in("roles").tag("Roles")

  def endpoints = Seq(createRoleEndpoint, deleteRoleEndpoint, listByPageEndpoint)

  val createRoleEndpoint = baseSecuredUserEndpoint.post
    .name("createRole")
    .summary("创建角色")
    .description("创建自定义角色")
    .in(jsonBody[CreateRoleRequest])
    .out(statusCode(StatusCode.Created))
    .out(jsonBody[Long])

  val deleteRoleEndpoint = baseSecuredUserEndpoint.delete
    .name("deleteRole")
    .summary("删除角色")
    .description("删除角色，超级管理员角色不允许删除")
    .in(path[Int]("id"))
    .out(statusCode(StatusCode.Ok))
    .out(jsonBody[Long])

  val listByPageEndpoint = baseSecuredUserEndpoint.get
    .name("listRoleByPage")
    .summary("分页获取角色")
    .description("分页的方式获取角色列表，支持排序")
    .in(query[Int]("page").default(1) / query[Int]("size").default(10) / query[Option[String]]("sort"))
    .out(jsonBody[PageDto[RoleDto]])

}
