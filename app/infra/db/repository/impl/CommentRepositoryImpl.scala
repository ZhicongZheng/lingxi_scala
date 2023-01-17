package infra.db.repository.impl

import domain.comment.{Comment, CommentRepository}

import scala.concurrent.Future

class CommentRepositoryImpl extends CommentRepository{
  override def save(domain: Comment): Future[Long] = ???

  override def get(id: Long): Future[Option[Comment]] = ???

  override def remove(id: Long): Future[Unit] = ???
}
