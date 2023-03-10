package interfaces.dto

import domain.comment.Comment
import infra.db.po.{ArticlePo, CommentsPo}

case class CommentEmailInfo(article: ArticlePo, replyPo: Option[CommentsPo], current: Comment)
