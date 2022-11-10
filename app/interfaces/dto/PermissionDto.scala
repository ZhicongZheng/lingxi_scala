package interfaces.dto

import domain.auth.entity.Permission
import play.api.libs.json.Json

import java.time.LocalDateTime
import scala.language.implicitConversions

case class PermissionDto(
  id: Long,
  `type`: String,
  value: String,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
)

object PermissionDto {
  implicit val format = Json.format[PermissionDto]

  implicit def fromDo(p: Permission): PermissionDto =
    PermissionDto(p.id, p.`type`, p.value, p.createBy, p.updateBy, p.createAt, p.updateAt)

  implicit def fromDoSeq(seq: Seq[Permission]): Seq[PermissionDto] = seq.map(p => PermissionDto.fromDo(p))
}
