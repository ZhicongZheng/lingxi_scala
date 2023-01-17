package domain.action

import domain.BaseEntity

import java.time.LocalDateTime

final case class Action(
  id: Long,
  typ: Int,
  resourceId: Long,
  remoteAddress: String,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseEntity

object Action {

  object Type {
    val VIEW_ARTICLE = 1
    val LICK_ARTICLE = 2
    val COMMENT      = 3
  }

}
