package interfaces.api.endpoints

import application.command.ActionCommand
import interfaces.dto.SiteInfoDto
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.play._
object SiteEndpoints {

  val baseEndpoint = endpoint.tag("Site Info API").errorOut(jsonBody[ErrorMessage])

  def endpoints = Seq(getSiteInfoEndpoint, onActionEndpoint)

  val getSiteInfoEndpoint = baseEndpoint.get
    .name("getSiteInfo")
    .summary("获取网站信息")
    .description("获取网站基本信息")
    .in("site")
    .out(statusCode(StatusCode.Ok))
    .out(jsonBody[SiteInfoDto])

  val onActionEndpoint = baseEndpoint.post
    .name("onAction")
    .summary("采集用户数据")
    .description("采集用户数据")
    .in("actions")
    .in(jsonBody[ActionCommand])
    .out(statusCode(StatusCode.Ok))

}
