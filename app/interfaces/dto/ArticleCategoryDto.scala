package interfaces.dto

import domain.article.ArticleCategory
import play.api.libs.json.{Json, OFormat}

import java.time.LocalDateTime
import scala.language.implicitConversions

case class ArticleCategoryDto(
  id: Long,
  name: String,
  parent: Long = -1,
  articleCount: Int = 0,
  children: Seq[ArticleCategoryDto] = Nil,
  createAt: LocalDateTime = LocalDateTime.now()
)

object ArticleCategoryDto {

  implicit val format: OFormat[ArticleCategoryDto] = Json.format[ArticleCategoryDto]

  implicit def fromPo(po: ArticleCategory): ArticleCategoryDto = ArticleCategoryDto(po.id, po.name, po.parent, 0, Nil, po.createAt)

}
