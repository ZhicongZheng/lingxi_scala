package infra.db.repository

import domain.article.{ArticleCategory, ArticleTag}
import infra.db.po.ArticlePo

import scala.concurrent.Future

trait ArticleQueryRepository extends QueryRepository[ArticlePo] {
  def listTagsById(tagIds: Seq[Long]): Future[Seq[ArticleTag]]

  def listTags(): Future[Seq[ArticleTag]]

  def getTagByName(name: String): Future[Option[ArticleTag]]

  def listCategorises(): Future[Seq[ArticleCategory]]

  def getCategoryById(categoryId: Long): Future[Option[ArticleCategory]]

  def getCategoryByName(name: String): Future[Option[ArticleCategory]]

}
