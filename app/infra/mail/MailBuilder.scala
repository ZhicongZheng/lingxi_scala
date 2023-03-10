package infra.mail

import interfaces.dto.CommentEmailInfo
import play.api.libs.mailer.Email

trait MailBuilder[A] {

  val fromUser = "1064275075@qq.com"
  def build(t: A): Email
}

object CommentMailBuilder extends MailBuilder[CommentEmailInfo] {

  override def build(t: CommentEmailInfo): Email = {
    val subject = if (t.replyPo.isEmpty) "网站评论提醒" else "评论回复提醒"
    val html    = if (t.replyPo.isEmpty) views.html.author.render(t) else views.html.commentReply.render(t)
    val toUser  = if (t.replyPo.isEmpty) fromUser else t.replyPo.get.userEmail.get
    Email(subject, fromUser, Seq(toUser), bodyHtml = Some(html.toString()))
  }
}
