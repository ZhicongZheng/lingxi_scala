package api.endpoints

import sttp.model.StatusCode
import sttp.tapir._
import _root_.auth.application.dto.LoginRequest
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.play.jsonBody

import javax.inject.Singleton

@Singleton
class UserEndpoints {

  private val baseUserEndpoint = endpoint
    .tag("Users API")
    .in("admin")

  private val baseSecuredUserEndpoint = securedWithBearerEndpoint
    .tag("Users API")
    .in("admin")

  val loginEndpoint = baseUserEndpoint.post
    .summary("用户登陆")
    .in("login")
    .in(jsonBody[LoginRequest])
    .out(statusCode(StatusCode.Ok))

}
