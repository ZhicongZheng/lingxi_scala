package interfaces.dto

import interfaces.dto.SiteInfoDto.SiteConfig
import play.api.libs.json.{Json, OFormat}

case class SiteInfoDto(
  articleCount: Long,
  categoryCount: Long,
  tagCount: Long,
  viewCount: Long,
  siteConfig: SiteConfig
)

object SiteInfoDto {

  case class SiteConfig(
    siteName: String,
    siteAddress: String,
    // 网站简介
    siteIntro: String,
    // 网站公告
    siteNotice: String,
    // 建站日期
    createSiteTime: String,
    // 备案号
    recordNumber: String,
    siteAuthor: String,
    aboutMe: String
  )

  val siteConfig: SiteConfig =
    SiteConfig("快乐乐园", "anyfun.top", "ZhengZhiCong 的后花园", "", "2023.02.22", "京ICP备2023002242号-1", "ZhengZhiCong", "Scala Java 服务端工程师")

  implicit val configFormat: OFormat[SiteConfig] = Json.format[SiteConfig]

  implicit val format: OFormat[SiteInfoDto] = Json.format[SiteInfoDto]
}
