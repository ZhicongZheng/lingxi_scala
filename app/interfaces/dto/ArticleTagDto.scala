package interfaces.dto

import domain.article.ArticleTag
import play.api.libs.json.{Json, OFormat}

import java.time.LocalDateTime

case class ArticleTagDto(id: Long, name: String, articleCount: Int = 0, createAt: LocalDateTime = LocalDateTime.now())

object ArticleTagDto {
  implicit val format: OFormat[ArticleTagDto] = Json.format[ArticleTagDto]

  def fromPo(po: ArticleTag): ArticleTagDto = ArticleTagDto(po.id, po.name, 0, po.createAt)
}
