package infra.db.po

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.time.LocalDateTime
import scala.language.implicitConversions

case class CategoryPo(id: Long, name: String, parent: Long = -1L, createAt: LocalDateTime = LocalDateTime.now())

object CategoryPo {

  class CategoryTable(_tableTag: Tag) extends Table[CategoryPo](_tableTag, "categories") with IdTable {
    def * = (id, name, parent, createAt).<>((CategoryPo.apply _).tupled, CategoryPo.unapply)

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255, varying = true))

    /** Database column parent SqlType(int8), Default(-1) */
    val parent: Rep[Long] = column[Long]("parent", O.Default(-1L))

    /** Database column create_at SqlType(timestamp) */
    val createAt: Rep[LocalDateTime] = column[LocalDateTime]("create_at")
  }

}
