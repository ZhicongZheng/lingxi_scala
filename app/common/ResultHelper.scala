package common

import common.result.Errors
import play.api.libs.json.{ JsObject, JsValue, Json }

object ResultHelper {

  def success(data: Option[JsValue] = None): JsObject =
    data match {
      case Some(d) => Json.obj("code" -> 0, "data" -> d)
      case None    => Json.obj()
    }

  def success(data: JsValue): JsValue = success(Some(data))

  def fail(error: Errors): JsValue = Json.obj("code" -> error.code, "message" -> error.message)

  def fail(ex: Throwable): JsObject =
    ex match {
      case _               => Json.obj("message" -> ex.getMessage)
    }


}
