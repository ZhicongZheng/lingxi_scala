package infra.db.po

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.time.LocalDateTime
import scala.language.implicitConversions

final case class TagPo(id: Long, name: String, createAt: LocalDateTime = LocalDateTime.now())

object TagPo {

  class TagTable(_tableTag: Tag) extends Table[TagPo](_tableTag, "tags") {
    def * = (id, name, createAt).<>((TagPo.apply _).tupled, TagPo.unapply)

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    /** Database column name SqlType(varchar), Length(256,true) */
    val name: Rep[String] = column[String]("name", O.Length(256, varying = true))

    /** Database column create_at SqlType(timestamp) */
    val createAt: Rep[LocalDateTime] = column[LocalDateTime]("create_at")
  }

}
