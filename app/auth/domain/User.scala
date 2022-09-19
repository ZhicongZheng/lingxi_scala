package auth.domain

import auth.domain.repository.table.UserTable
import play.api.libs.json.{Json, OFormat}

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

object User {

  implicit val format: OFormat[User] = Json.format[User]

  def apply(table: UserTable): User = {
    User(table.id, table.username, table.password, table.avatar, table.nickName, table.createBy, table.updateBy, table.createAt, table.updateAt)
  }

}
