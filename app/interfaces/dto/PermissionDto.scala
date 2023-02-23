package interfaces.dto

import domain.auth.Permission
import infra.db.po.PermissionPo
import play.api.libs.json.{Json, OFormat}
import io.scalaland.chimney.dsl._

import java.time.LocalDateTime
import scala.language.implicitConversions

case class PermissionDto(
  id: Long,
  `type`: String,
  value: String,
  name: String,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
)

object PermissionDto {
  implicit val format: OFormat[PermissionDto] = Json.format[PermissionDto]

  implicit def fromDo(p: Permission): PermissionDto = p.into[PermissionDto].transform

  implicit def fromPo(p: PermissionPo): PermissionDto = p.into[PermissionDto].transform

  implicit def fromDoSeq(seq: Seq[Permission]): Seq[PermissionDto] = seq.map(p => PermissionDto.fromDo(p))
}
