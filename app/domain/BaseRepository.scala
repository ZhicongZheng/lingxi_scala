package domain

import common.{Page, PageQuery}

import scala.concurrent.Future

trait BaseRepository[T <: BaseEntity] {

  def findById(id: Long): Future[Option[T]]

  def list(): Future[Seq[T]]

  def count(): Future[Int]

  def create(d: T): Future[Long]

  def update(d: T): Future[Int]

  def delete(id: Long): Future[Int]

  def listByPage(pageQuery: PageQuery): Future[Page[T]]

}
