package auth.repository.po

import auth.domain.{BaseInfo, User}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.time.LocalDateTime
import scala.language.implicitConversions

final case class UserPo(
  id: Long,
  username: String,
  password: String,
  avatar: String,
  nickName: String,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseInfo
    with BasePo[User] {

  implicit override def toDo: User =
    User(id, username, password, avatar, nickName, None, None, createBy, updateBy, createAt, updateAt)

}

object UserPo {

  implicit def fromDo(t: User): UserPo =
    UserPo(t.id, t.username, t.password, t.avatar, t.nickName, t.createBy, t.updateBy, t.createAt, t.updateAt)

  class UserTable(tag: Tag) extends Table[UserPo](tag, "users") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def username = column[String]("username")

    def password = column[String]("password")

    def avatar = column[String]("avatar")

    def nickName = column[String]("nick_name")

    def createBy = column[Long]("create_by")

    def updateBy = column[Long]("update_by")

    def createAt = column[LocalDateTime]("create_at")

    def updateAt = column[LocalDateTime]("update_at")


    override def * = (
      id,
      username,
      password,
      avatar,
      nickName,
      createBy,
      updateBy,
      createAt,
      updateAt
    ) <> ((UserPo.apply _).tupled, UserPo.unapply)
  }

}
