package auth.domain

import java.time.LocalDateTime

final case class Permission(id: Long,
                            `type`: String,
                            value: String,
                            roles: Option[Seq[Role]] = None,
                            createBy: Long = 0L,
                            updateBy: Long = 0L,
                            createAt: LocalDateTime = LocalDateTime.now(),
                            updateAt: Option[LocalDateTime] = None) extends BaseInfo {


}
