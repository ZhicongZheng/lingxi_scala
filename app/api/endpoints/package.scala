package api

import play.api.libs.json.Json
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.play._

package object endpoints {

  case class ErrorMessage(code: Int, message: String)

  implicit val format = Json.format[ErrorMessage]

  // 需要验证 token 的接口
  val securedWithBearerEndpoint = endpoint
    .securityIn(auth.bearer[String]())
    .errorOut(statusCode(StatusCode.Unauthorized))
    .errorOut(jsonBody[ErrorMessage])

}
