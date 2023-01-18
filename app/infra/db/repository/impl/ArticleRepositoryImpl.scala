package infra.db.repository.impl

import domain.article.{Article, ArticleRepository}

import scala.concurrent.Future

class ArticleRepositoryImpl extends ArticleRepository {
  override def save(domain: Article): Future[Long] = ???

  override def get(id: Long): Future[Option[Article]] = ???

  override def remove(id: Long): Future[Unit] = ???
}
