package common

import play.api.libs.json.{JsObject, JsValue, Json}

object ResultHelper {

  def success(data: Option[JsValue] = None): JsObject = {
    Json.obj("code" -> 0, "data" -> data)
  }

  def fail(ex: Throwable): JsObject = {
    Json.obj("code" -> 0, "message" -> ex.getMessage)
  }

  def fail(response: ResponseCode): JsObject = {
    Json.obj("code" -> response.getCode, "message" -> response.getMessage)
  }

}
