package auth.domain.repository.table

import auth.domain.User
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.time.LocalDateTime

class UserTable(tag: Tag) extends Table[User](tag, "users") {

  def id = column[Long]("id", O.PrimaryKey)

  def username = column[String]("username")

  def password = column[String]("password")

  def avatar = column[String]("avatar")

  def nickName = column[String]("nick_name")

  def createBy = column[Long]("create_by")

  def updateBy = column[Long]("update_by")

  def createAt = column[LocalDateTime]("create_at")

  def updateAt = column[Option[LocalDateTime]]("update_at")

  override def * = (id, username, password, avatar, nickName, createBy, updateBy, createAt, updateAt) <> (User.tupled, User.unapply)
}
