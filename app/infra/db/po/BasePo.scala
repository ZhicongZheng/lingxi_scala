package infra.db.po

import domain.BaseEntity

trait BasePo[T] extends BaseEntity {

  implicit def toDo: T

}
