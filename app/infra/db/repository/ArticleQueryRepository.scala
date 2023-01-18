package infra.db.repository

import infra.db.po.{ArticlePo, CategoryPo, TagPo}

import scala.concurrent.Future

trait ArticleQueryRepository extends QueryRepository[ArticlePo] {
  def listTagsById(tagIds: Seq[Long]): Future[Seq[TagPo]]

  def getCategoryById(categoryId: Long): Future[Option[CategoryPo]]

}
