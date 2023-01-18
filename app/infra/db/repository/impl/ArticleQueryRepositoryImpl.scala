package infra.db.repository.impl

import common.{Constant, Page, PageQuery}
import domain.action.Action
import domain.article.{Article, ArticleRepository}
import infra.db.assembler.ArticleAssembler._
import infra.db.po.ActionPo.ActionTable
import infra.db.po.{ArticlePo, CategoryPo, TagPo}
import infra.db.po.ArticlePo.ArticleTable
import infra.db.po.CategoryPo.CategoryTable
import infra.db.po.TagPo.TagTable
import infra.db.repository.ArticleQueryRepository
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ArticleQueryRepositoryImpl @Inject() (private val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends ArticleQueryRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig: DatabaseConfig[PostgresProfile] = dbConfigProvider.get[PostgresProfile]

  private val articles   = TableQuery[ArticleTable]
  private val tags       = TableQuery[TagTable]
  private val categories = TableQuery[CategoryTable]
  private val actions    = TableQuery[ActionTable]

  override def get(id: Long): Future[Option[ArticlePo]] = db.run(articles.filter(_.id === id).result.headOption)

  override def list(): Future[Seq[ArticlePo]] = db.run(articles.result)

  override def count(): Future[Int] = db.run(articles.size.result)

  override def listByPage(pageQuery: PageQuery): Future[Page[ArticlePo]] = ???

  override def listTagsById(tagIds: Seq[Long]): Future[Seq[TagPo]] = db.run(tags.filter(_.id inSet tagIds.toSet).result)

  override def getCategoryById(categoryId: Long): Future[Option[CategoryPo]] =
    db.run(categories.filter(_.id === categoryId).result.headOption)
}
