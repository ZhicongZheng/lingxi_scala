package auth.repository.po

import auth.domain.{BaseInfo, User}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.time.LocalDateTime

final case class UserPo(id: Long,
                        username: String,
                        password: String,
                        avatar: String,
                        nickName: String,
                        createBy: Long = 0L,
                        updateBy: Long = 0L,
                        createAt: LocalDateTime = LocalDateTime.now(),
                        updateAt: Option[LocalDateTime] = None) extends BaseInfo with BasePo[User]{

  override def toDo: User = {
    User(id, username, password, avatar, nickName, None, createBy, updateBy, createAt, updateAt)
  }

}


class UserTable(tag: Tag) extends Table[UserPo](tag, "users") {

  def id = column[Long]("id", O.PrimaryKey)

  def username = column[String]("username")

  def password = column[String]("password")

  def avatar = column[String]("avatar")

  def nickName = column[String]("nick_name")

  def createBy = column[Long]("create_by")

  def updateBy = column[Long]("update_by")

  def createAt = column[LocalDateTime]("create_at")

  def updateAt = column[Option[LocalDateTime]]("update_at")

  override def * = (id, username, password, avatar, nickName, createBy, updateBy, createAt, updateAt) <> (UserPo.tupled, UserPo.unapply)
}