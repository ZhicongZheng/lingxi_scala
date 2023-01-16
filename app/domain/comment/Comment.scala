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
  userEmail: String,
  // 评论下面的回复
  reply: Seq[Comment] = Nil,
  // 评论的字段（文章）
  resourceId: Long,
  // 评论用户的ip
  remoteAddress: String,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseEntity
