package interfaces

import play.api.libs.json.Reads.localDateTimeReads
import play.api.libs.json.{Reads, Writes}
import play.api.libs.json.Writes.temporalWrites

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

package object dto {

  val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  implicit def localDateTimeFormat: Writes[LocalDateTime] =
    temporalWrites[LocalDateTime, DateTimeFormatter](dateTimeFormatter)

  implicit val DefaultLocalDateTimeReads: Reads[LocalDateTime] = localDateTimeReads(dateTimeFormatter)

}
