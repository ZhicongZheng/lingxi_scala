package infra.db.po

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.time.LocalDateTime
import scala.language.implicitConversions

final case class CommentsPo(
  id: Long,
  content: String,
  userName: String = "",
  userEmail: Option[String] = None,
  replyTo: Long = -1L,
  resourceId: Long,
  remoteAddress: String,
  allowNotify: Boolean = false,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
)
object CommentsPo {

  class CommentTable(_tableTag: Tag) extends Table[CommentsPo](_tableTag, "comments") {
    def * = (id, content, userName, userEmail, replyTo, resourceId, remoteAddress, allowNotify, createBy, updateBy, createAt, updateAt).<>(
      (CommentsPo.apply _).tupled,
      CommentsPo.unapply
    )

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    /** Database column content SqlType(varchar) */
    val content: Rep[String] = column[String]("content")

    /** Database column user_name SqlType(varchar), Default() */
    val userName: Rep[String] = column[String]("user_name", O.Default(""))

    /** Database column user_email SqlType(varchar), Default(None) */
    val userEmail: Rep[Option[String]] = column[Option[String]]("user_email", O.Default(None))

    /** Database column reply_to SqlType(int8), Default(-1) */
    val replyTo: Rep[Long] = column[Long]("reply_to", O.Default(-1L))

    /** Database column resource_id SqlType(int8) */
    val resourceId: Rep[Long] = column[Long]("resource_id")

    /** Database column remote_address SqlType(inet), Length(2147483647,false) */
    val remoteAddress: Rep[String] = column[String]("remote_address", O.Length(2147483647, varying = false))

    /** Database column allow_notify SqlType(bool), Default(false) */
    val allowNotify: Rep[Boolean] = column[Boolean]("allow_notify", O.Default(false))

    /** Database column create_by SqlType(int8), Default(0) */
    val createBy: Rep[Long] = column[Long]("create_by", O.Default(0L))

    /** Database column update_by SqlType(int8), Default(0) */
    val updateBy: Rep[Long] = column[Long]("update_by", O.Default(0L))

    /** Database column create_at SqlType(timestamp) */
    val createAt: Rep[LocalDateTime] = column[LocalDateTime]("create_at")

    /** Database column update_at SqlType(timestamp) */
    val updateAt: Rep[LocalDateTime] = column[LocalDateTime]("update_at")
  }
}
