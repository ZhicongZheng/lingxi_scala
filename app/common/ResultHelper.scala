package common

import common.exceptions.BizException
import play.api.libs.json.{JsObject, JsValue, Json}

object ResultHelper {

  def success(data: Option[JsValue] = None): JsObject =
    data match {
      case Some(d) => Json.obj("code" -> 0, "data" -> d)
      case None    => Json.obj("code" -> 0)
    }

  def success(data: JsValue): JsValue =
    success(Some(data))

  def fail(ex: Throwable): JsObject =
    ex match {
      case e: BizException => fail(e.getResponseCode)
      case _               => Json.obj("code" -> 0, "message" -> ex.getMessage)
    }

  def fail(response: ResponseCode): JsObject =
    Json.obj("code" -> response.getCode, "message" -> response.getMessage)

}
