package common

import common.result.Errors
import play.api.libs.json.{ Json, Writes }
import play.api.mvc.Results.{ InternalServerError, Ok }
import play.api.mvc._

object Results {

  def success[T](data: T)(implicit tjs: Writes[T]): Result = Ok(Json.obj("code" -> 0, "data" -> Json.toJson(data)))

  def fail(error: Errors): Result = error.httpStatus(Json.obj("code" -> error.code, "message" -> error.message))

  def fail(ex: Throwable): Result = InternalServerError(Json.obj("message" -> ex.getMessage))

}
