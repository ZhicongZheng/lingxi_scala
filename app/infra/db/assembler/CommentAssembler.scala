package infra.db.assembler

import domain.comment.Comment
import infra.db.po.CommentsPo

import scala.language.implicitConversions

object CommentAssembler {

  implicit def toPo(c: Comment): CommentsPo = CommentsPo(
    c.id,
    c.content,
    c.userName,
    c.userEmail,
    c.replyTo,
    c.resourceId,
    c.remoteAddress,
    c.allowNotify,
    c.createBy,
    c.updateBy,
    c.updateAt,
    c.updateAt
  )

}
