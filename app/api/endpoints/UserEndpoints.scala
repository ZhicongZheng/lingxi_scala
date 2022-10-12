package api.endpoints

import _root_.auth.application.dto.LoginRequest
import sttp.model.{ HeaderNames, StatusCode }
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.play.jsonBody

import javax.inject.Singleton

@Singleton
class UserEndpoints {

  private val baseUserEndpoint = endpoint
    .tag("Users API")
    .in("users")

  private val baseSecuredUserEndpoint = securedWithBearerEndpoint
    .tag("Users API")
    .in("users")

  val loginEndpoint = baseUserEndpoint.post
    .summary("用户登陆")
    .in("login")
    .in(jsonBody[LoginRequest])
    .out(statusCode(StatusCode.Ok))
    .out(header[String](HeaderNames.Authorization))

}
