package auth.domain.repository.table

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.time.LocalDateTime

final case class UserTable(override val id: Long,
                           username: String,
                           password: String,
                           avatar: String,
                           nickName: String,
                           override val createBy: Long = 0L,
                           override val updateBy: Long = 0L,
                           override val createAt: LocalDateTime = LocalDateTime.now(),
                           override val updateAt: Option[LocalDateTime] = None) extends BaseTable {

}

class UserTableSchema(tag: Tag) extends Table[UserTable](tag, "users") {

  def id = column[Long]("id", O.PrimaryKey)

  def username = column[String]("username")

  def password = column[String]("password")

  def avatar = column[String]("avatar")

  def nickName = column[String]("nick_name")

  def createBy = column[Long]("create_by")

  def updateBy = column[Long]("update_by")

  def createAt = column[LocalDateTime]("create_at")

  def updateAt = column[Option[LocalDateTime]]("update_at")

  override def * = (id, username, password, avatar, nickName, createBy, updateBy, createAt, updateAt) <> (UserTable.tupled, UserTable.unapply)
}