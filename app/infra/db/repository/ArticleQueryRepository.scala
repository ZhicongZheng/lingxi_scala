package infra.db.repository

import common.Page
import domain.article.{ArticleCategory, ArticleTag}
import infra.db.po.ArticlePo
import interfaces.dto.ArticlePageQuery

import scala.concurrent.Future

trait ArticleQueryRepository extends QueryRepository[ArticlePo] {

  def listTagsById(tagIds: Seq[Long]): Future[Seq[ArticleTag]]

  def getArticleTagMap(articleIds: Seq[Long]): Future[Map[Long, Seq[ArticleTag]]]

  def listArticleByPage(query: ArticlePageQuery): Future[Page[ArticlePo]]

  def listTagsByArticle(articleId: Long): Future[Seq[ArticleTag]]

  def listCategoryByIds(ids: Seq[Long]): Future[Seq[ArticleCategory]]

  def listTags(): Future[Seq[ArticleTag]]

  def getTagByName(name: String): Future[Option[ArticleTag]]

  def listCategorises(): Future[Seq[ArticleCategory]]

  def getCategoryById(categoryId: Long): Future[Option[ArticleCategory]]

  def getCategoryByName(name: String): Future[Option[ArticleCategory]]

}
