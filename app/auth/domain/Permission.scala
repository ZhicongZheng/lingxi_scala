package auth.domain

import java.time.LocalDateTime

final case class Permission(
  id: Long,
  `type`: String,
  value: String,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseInfo {}

object Permission {}
