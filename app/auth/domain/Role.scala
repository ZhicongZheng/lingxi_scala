package auth.domain

import java.time.LocalDateTime

final case class Role(
  id: Long,
  code: String,
  name: String,
  users: Option[Seq[User]] = None,
  Permissions: Option[Seq[Permission]] = None,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseInfo
