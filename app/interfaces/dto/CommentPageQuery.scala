package interfaces.dto

import common.PageQuery
import play.api.libs.json.{Json, OFormat}

case class CommentPageQuery(page: Int, size: Int, resourceId: Long, typ: Option[Int] = None, parent: Option[Long] = None) extends PageQuery

object CommentPageQuery {

  implicit val format: OFormat[CommentPageQuery] = Json.format[CommentPageQuery]
}
