package auth.domain

import java.time.LocalDateTime

final case class User(override val id: Long,
                      username: String,
                      password: String,
                      avatar: String,
                      nickName: String,
                      override val createBy: Long = 0L,
                      override val updateBy: Long = 0L,
                      override val createAt: LocalDateTime = LocalDateTime.now(),
                      override val updateAt: Option[LocalDateTime] = None
                     ) extends BaseEntity {

}
