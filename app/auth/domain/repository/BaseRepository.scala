package auth.domain.repository

import auth.domain.BaseInfo

import scala.concurrent.Future

trait BaseRepository[T <: BaseInfo] {

  def findById(id: Long): Future[Option[T]]

  def list(): Future[Seq[T]]

  def count(): Future[Int]

  def create(d: T): Future[Long]

  def update(d: T): Future[Int]

  def delete(id: Long): Future[Int]

}
