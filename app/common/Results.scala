package common

import common.result.Errors
import play.api.Logging
import play.api.libs.json.{Json, Writes}
import play.api.mvc.Results._
import play.api.mvc._

object Results extends Logging {

  def success[T](data: T)(implicit tjs: Writes[T]): Result = Ok(Json.toJson(data))

  def fail(error: Errors): Result = error.httpStatus(Json.obj("code" -> error.code, "message" -> error.message))

  def fail(ex: Throwable): Result = {
    logger.error("",ex)
    InternalServerError(Json.obj("message" -> ex.getMessage))
  }

}
