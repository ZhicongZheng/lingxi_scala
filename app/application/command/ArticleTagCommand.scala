package application.command

import common.Constant
import domain.article.ArticleTag
import play.api.libs.json.{Json, OFormat}

import scala.language.implicitConversions

case class ArticleTagCommand(id: Option[Long] = None, name: String)

object ArticleTagCommand {

  implicit val format: OFormat[ArticleTagCommand] = Json.format[ArticleTagCommand]

  implicit def convert(cmd: ArticleTagCommand): ArticleTag = ArticleTag(cmd.id.getOrElse(Constant.domainCreateId), cmd.name)
}
