package auth.domain

import java.time.LocalDateTime

final case class Role(override val id: Long,
                      code: String,
                      name: String,
                      users: Seq[User] = Seq.empty,
                      Permissions: Seq[Permission] = Seq.empty,
                      override val createBy: Long = 0L,
                      override val updateBy: Long = 0L,
                      override val createAt: LocalDateTime = LocalDateTime.now(),
                      override val updateAt: Option[LocalDateTime] = None) extends BaseEntity {



}
