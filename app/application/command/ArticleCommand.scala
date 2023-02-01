package application.command

import common.Constant
import domain.article.{Article, ArticleCategory, ArticleTag}
import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class ArticleCommand(
  id: Option[Long] = None,
  title: String,
  introduction: String,
  frontCover: Option[String] = None,
  tags: Seq[Long] = Nil,
  category: Option[Long] = None,
  contentMd: String = "",
  contentHtml: String = ""
)

object ArticleCommand {

  implicit val format: OFormat[ArticleCommand] = Json.format[ArticleCommand]

  implicit def toDo(cmd: ArticleCommand): Article =
    Article(
      id = cmd.id.getOrElse(Constant.domainCreateId),
      title = cmd.title,
      introduction = cmd.introduction,
      frontCover = cmd.frontCover,
      tags = cmd.tags.map(ArticleTag.justId),
      category = cmd.category.map(ArticleCategory.justId),
      contentMd = cmd.contentMd,
      contentHtml = cmd.contentHtml
    )
}
