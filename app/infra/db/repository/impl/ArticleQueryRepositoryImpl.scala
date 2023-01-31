package infra.db.repository.impl

import common.{Page, PageQuery}
import domain.article.{ArticleCategory, ArticleTag}
import infra.db.po.ArticlePo.ArticleTable
import infra.db.po.{ArticlePo, CategoryTable, TagTable}
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

  override def get(id: Long): Future[Option[ArticlePo]] = db.run(articles.filter(_.id === id).result.headOption)

  override def list(): Future[Seq[ArticlePo]] = db.run(articles.result)

  override def count(): Future[Int] = db.run(articles.size.result)

  override def listByPage(pageQuery: PageQuery): Future[Page[ArticlePo]] = ???

  override def listTagsById(tagIds: Seq[Long]): Future[Seq[ArticleTag]] = db.run(tags.filter(_.id inSet tagIds.toSet).result)

  override def getCategoryById(categoryId: Long): Future[Option[ArticleCategory]] =
    db.run(categories.filter(_.id === categoryId).result.headOption)

  override def listTags(): Future[Seq[ArticleTag]] = db.run(tags.result)

  override def listCategorises(): Future[Seq[ArticleCategory]] = db.run(categories.result)

  override def getTagByName(name: String): Future[Option[ArticleTag]] = db.run(tags.filter(_.name === name).result.headOption)

  override def getCategoryByName(name: String): Future[Option[ArticleCategory]] =
    db.run(categories.filter(_.name === name).result.headOption)
}
