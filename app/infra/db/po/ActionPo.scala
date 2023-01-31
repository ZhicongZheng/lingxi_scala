package infra.db.po

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.time.LocalDateTime
import scala.language.implicitConversions

final case class ActionPo(
  id: Long,
  typ: Int,
  resourceId: Long,
  remoteAddress: String,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
)

object ActionPo {

  /** Table description of table actions. Objects of this class serve as prototypes for rows in queries. */
  class ActionTable(_tableTag: Tag) extends Table[ActionPo](_tableTag, "actions") {
    def * = (id, typ, resourceId, remoteAddress, createBy, updateBy, createAt, updateAt).<>((ActionPo.apply _).tupled, ActionPo.unapply)

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    /** Database column typ SqlType(int4) */
    val typ: Rep[Int] = column[Int]("typ")

    /** Database column source_id SqlType(int8) */
    val resourceId: Rep[Long] = column[Long]("resource_id")

    /** Database column remote_address SqlType(inet), Length(2147483647,false) */
    val remoteAddress: Rep[String] = column[String]("remote_address", O.Length(2147483647, varying = false))

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
