//package auth.domain.repository.table
//
//import auth.domain.Role
//import slick.jdbc.PostgresProfile.api._
//import slick.lifted.Tag
//
//import java.time.LocalDateTime
//
//
//class RoleTable(tag: Tag) extends Table[Role](tag, "roles") {
//
//  def id = column[Long]("id", O.PrimaryKey)
//
//  def code = column[String]("code")
//
//  def name = column[String]("name")
//
//  def createBy = column[Long]("create_by")
//
//  def updateBy = column[Long]("update_by")
//
//  def createAt = column[LocalDateTime]("create_at")
//
//  def updateAt = column[Option[LocalDateTime]]("update_at")
//
//  override def * = (id, code, name, createBy, updateBy, createAt, updateAt) <> (Role.tupled, Role.unapply)
//}
