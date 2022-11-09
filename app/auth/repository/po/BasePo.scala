package auth.repository.po

import auth.domain.BaseInfo

trait BasePo[T] extends BaseInfo {

  implicit def toDo: T

}
