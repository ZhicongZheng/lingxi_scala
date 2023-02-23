package domain.auth

import domain.BaseEntity
import play.api.libs.json.{Json, OFormat}

import java.time.LocalDateTime

final case class Permission(
  id: Long,
  `type`: String,
  value: String,
  name: String,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseEntity

object Permission {

  implicit val format: OFormat[Permission] = Json.format[Permission]

  def justId(id: Long): Permission = Permission(id, "", "", "")
}
