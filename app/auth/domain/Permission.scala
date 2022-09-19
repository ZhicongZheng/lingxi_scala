package auth.domain

import java.time.LocalDateTime

final case class Permission(override val id: Long,
                            `type`: String,
                            value: String,
                            roles: Seq[Role] = Seq.empty,
                            override val createBy: Long = 0L,
                            override val updateBy: Long = 0L,
                            override val createAt: LocalDateTime = LocalDateTime.now(),
                            override val updateAt: Option[LocalDateTime] = None) extends BaseEntity {


}
