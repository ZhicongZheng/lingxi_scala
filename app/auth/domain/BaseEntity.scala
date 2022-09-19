package auth.domain

import java.time.LocalDateTime


/**
 * 基础领域对象
 */
trait BaseEntity {

  val id: Long
  val createBy: Long
  val updateBy: Long
  val createAt: LocalDateTime
  val updateAt: Option[LocalDateTime]

}

object BaseEntity {
  class Base(override val id: Long,
             override val createBy: Long = 0L,
             override val updateBy: Long = 0L,
             override val createAt: LocalDateTime = LocalDateTime.now(),
             override val updateAt: Option[LocalDateTime] = None) extends BaseEntity
}
