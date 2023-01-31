package application.command

import common.Constant
import domain.article.ArticleCategory
import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class ArticleCategoryCommand(id: Option[Long] = None, name: String)

object ArticleCategoryCommand {
  implicit val format: OFormat[ArticleCategoryCommand] = Json.format[ArticleCategoryCommand]

  implicit def convert(cmd: ArticleCategoryCommand): ArticleCategory = ArticleCategory(cmd.id.getOrElse(Constant.domainCreateId), cmd.name)
}
