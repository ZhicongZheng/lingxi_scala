package common

trait PageAble[T] {

  def page: Int

  def size: Int

  def totalPages: Int = (totalCount / size) + 1

  def totalCount: Int

  def sort: Option[String] = None

  def hasPrevious: Boolean = totalCount > 0 && page != totalPages

  def hasNext: Boolean = totalCount > (page * size)

  def content: Seq[T]
}
