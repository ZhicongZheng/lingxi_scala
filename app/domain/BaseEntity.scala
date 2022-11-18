package domain

import java.time.LocalDateTime

/** 基础领域对象
 */
trait BaseEntity {

  def id: Long

  def createBy: Long

  def updateBy(): Long

  def createAt: LocalDateTime

  def updateAt(): LocalDateTime

}
