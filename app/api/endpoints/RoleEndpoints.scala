package api.endpoints

import sttp.tapir._
import _root_.auth.application.dto.CreateRoleRequest
import sttp.model.StatusCode
import sttp.tapir.json.play.jsonBody
import sttp.tapir.generic.auto.schemaForCaseClass

object RoleEndpoints {

  private val baseSecuredUserEndpoint = securedWithBearerEndpoint.in("roles").tag("Roles API")

  def endpoints = Seq(createRoleEndpoint)

  val createRoleEndpoint = baseSecuredUserEndpoint.post
    .name("createRole")
    .summary("创建角色")
    .description("创建自定义角色")
    .in(jsonBody[CreateRoleRequest])
    .out(statusCode(StatusCode.Created))
    .out(jsonBody[Long])

}
