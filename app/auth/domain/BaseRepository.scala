package auth.domain

import scala.concurrent.Future

trait BaseRepository[T <: BaseInfo] {

  def findById(id: Long): Future[Option[T]]

  def list(): Future[Seq[T]]

  def count(): Future[Int]

  def create(d: T): Future[T]

  def update(d: T): Future[Long]

  def delete(id: Long): Future[Long]

}
