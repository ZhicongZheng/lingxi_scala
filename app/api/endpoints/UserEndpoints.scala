package api.endpoints

import sttp.model.StatusCode
import sttp.tapir.{Endpoint, endpoint, statusCode}

import javax.inject.Singleton

@Singleton
class UserEndpoints {

  private val baseUserEndpoint = endpoint
    .tag("Users API")
    .in("admin")

  private val baseSecuredUserEndpoint = securedWithBearerEndpoint
    .tag("Users API")
    .in("admin")

  val loginEndpoint: Endpoint[Unit, Unit, Unit, Unit, Any] = baseUserEndpoint.post
    .summary("用户登陆")
    .in("login")
    .out(statusCode(StatusCode.Ok))

}
