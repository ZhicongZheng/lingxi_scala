package common

import interfaces.dto.{RoleDto, UserDto}
import play.api.libs.json.{Json, OFormat}

case class Page[T](
  page: Int = 1,
  size: Int = 10,
  totalCount: Int = -1,
  data: Seq[T] = Nil,
  totalPages: Int = 0,
  hasPrevious: Boolean = false,
  hasNext: Boolean = false
) {
  def map[B](f: T => B): Page[B] = copy(data = data.map(c => f.apply(c)))
}

object Page {

  def apply[T](page: Int, size: Int, totalCount: Int, data: Seq[T]): Page[T] = {
    val totalPages = (totalCount / size) + 1
    Page(
      page,
      size,
      totalCount,
      data,
      totalPages = totalPages,
      hasPrevious = totalCount > 0 && page != totalPages,
      hasNext = totalCount > (page * size)
    )
  }
}

case class PageQuery(page: Int, size: Int, sort: Option[String] = None) {
  def offset: Int = (page - 1) * size
  def limit: Int  = size
}
