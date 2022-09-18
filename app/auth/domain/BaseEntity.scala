package auth.domain

import java.time.LocalDateTime


/**
 *  基础领域对象
 */
trait BaseEntity {

  val id: Long
  val createBy: Long = 0L
  val updateBy: Long = 0L
  val createAt: LocalDateTime = LocalDateTime.now()
  val updateAt: Option[LocalDateTime] = None

}
