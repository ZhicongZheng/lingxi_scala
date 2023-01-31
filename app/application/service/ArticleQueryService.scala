package application.service

import domain.article.{ArticleCategory, ArticleTag}
import infra.db.repository.ArticleQueryRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class ArticleQueryService @Inject() (articleQueryRepository: ArticleQueryRepository) {

  def listTags(): Future[Seq[ArticleTag]] = articleQueryRepository.listTags()

  def listCategorises(): Future[Seq[ArticleCategory]] = articleQueryRepository.listCategorises()

}
