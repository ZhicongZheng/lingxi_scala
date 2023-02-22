package domain.comment

import domain.BaseEntity

import java.time.LocalDateTime

final case class Comment(
  id: Long,
  // 评论内容
  content: String,
  // 评论用户名
  userName: String,
  // 用户邮箱
  userEmail: Option[String] = None,
  // 评论下面的回复
  reply: Seq[Comment] = Nil,
  // 回复的评论id
  replyTo: Long = -1,
  // 评论的资源（文章）
  resourceId: Long,
  // 评论用户的ip
  remoteAddress: String,
  // 评论有回复时是否允许通知
  allowNotify: Boolean = false,
  createAt: LocalDateTime = LocalDateTime.now()
) extends BaseEntity
