package api

import sttp.model.StatusCode
import sttp.tapir.{ endpoint, statusCode, Endpoint, TapirAuth }

package object endpoints {

  // 需要验证 token 的接口
  val securedWithBearerEndpoint: Endpoint[String, Unit, Unit, Unit, Any] = endpoint
    .securityIn(TapirAuth.bearer[String]())
    .errorOut(statusCode(StatusCode.Unauthorized))

}
