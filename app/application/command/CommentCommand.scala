package application.command

import common.Constant.domainCreateId
import common.Ip2Region
import domain.comment.Comment
import domain.comment.Comment.Type
import io.scalaland.chimney.dsl._
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.Request

case class CommentCommand(
  id: Option[Long] = None,
  typ: Int = Type.comment,
  content: String,
  userName: String,
  userEmail: Option[String] = None,
  replyTo: Option[Long] = None,
  replyUser: Option[String] = None,
  resourceId: Long,
  allowNotify: Boolean = false
) {}

object CommentCommand {

  implicit val format: OFormat[CommentCommand] = Json.format[CommentCommand]

  implicit def toDo(cmd: CommentCommand)(implicit request: Request[_]): Comment = cmd
    .into[Comment]
    .withFieldComputed(_.id, _.id.getOrElse(domainCreateId))
    .withFieldComputed(_.replyTo, _.replyTo.getOrElse(-1L))
    .withFieldComputed(_.replyUser, _.replyUser.getOrElse(""))
    .withFieldConst(_.remoteIp, Ip2Region.parseIp(request))
    .withFieldConst(_.remoteAddress, Ip2Region.parseAddress(request))
    .transform

}
