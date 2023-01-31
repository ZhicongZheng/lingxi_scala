package infra.db.po

import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

import java.time.LocalDateTime
import scala.language.implicitConversions

final case class ArticlePo(
  id: Long,
  title: String,
  introduction: String = "",
  frontCover: Option[String] = None,
  contentMd: String = "",
  contentHtml: String = "",
  status: Int = 0,
  tags: String,
  category: Option[Long] = None,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends Po

object ArticlePo {

  /** Table description of table articles. Objects of this class serve as prototypes for rows in queries. */
  class ArticleTable(tag: Tag) extends Table[ArticlePo](tag, "articles") with BaseTable {

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    /** Database column title SqlType(varchar) */
    val title: Rep[String] = column[String]("title")

    /** Database column introduction SqlType(varchar), Default() */
    val introduction: Rep[String] = column[String]("introduction", O.Default(""))

    /** Database column front_cover SqlType(varchar), Default(None) */
    val frontCover: Rep[Option[String]] = column[Option[String]]("front_cover", O.Default(None))

    /** Database column content_md SqlType(varchar), Default() */
    val contentMd: Rep[String] = column[String]("content_md", O.Default(""))

    /** Database column content_html SqlType(varchar), Default() */
    val contentHtml: Rep[String] = column[String]("content_html", O.Default(""))

    /** Database column status SqlType(int2), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))

    /** Database column tags SqlType(_int8), Length(19,false) */
    val tags: Rep[String] = column[String]("tags", O.Length(19, varying = false))

    /** Database column category SqlType(int8), Default(None) */
    val category: Rep[Option[Long]] = column[Option[Long]]("category", O.Default(None))

    /** Database column create_by SqlType(int8), Default(0) */
    val createBy: Rep[Long] = column[Long]("create_by", O.Default(0L))

    /** Database column update_by SqlType(int8), Default(0) */
    val updateBy: Rep[Long] = column[Long]("update_by", O.Default(0L))

    /** Database column create_at SqlType(timestamp) */
    val createAt: Rep[LocalDateTime] = column[LocalDateTime]("create_at")

    /** Database column update_at SqlType(timestamp) */
    val updateAt: Rep[LocalDateTime] = column[LocalDateTime]("update_at")

    override def * : ProvenShape[ArticlePo] = (
      id,
      title,
      introduction,
      frontCover,
      contentMd,
      contentHtml,
      status,
      tags,
      category,
      createBy,
      updateBy,
      createAt,
      updateAt
    ) <> ((ArticlePo.apply _).tupled, ArticlePo.unapply)
  }
}
