package auth.repository.po

trait BasePo[T] {

  implicit def toDo: T

}
