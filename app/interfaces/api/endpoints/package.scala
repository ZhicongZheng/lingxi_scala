package interfaces.api

import play.api.libs.json.{Json, OFormat}
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.play._

package object endpoints {

  case class ErrorMessage(code: Int, message: String)

  implicit val format: OFormat[ErrorMessage] = Json.format[ErrorMessage]

  // 需要验证 cookie 的接口
  val securedWithBearerEndpoint = endpoint
    .securityIn(auth.apiKey[String](cookie("cookie")))
    .errorOut(statusCode(StatusCode.Unauthorized))
    .errorOut(jsonBody[ErrorMessage])

}
