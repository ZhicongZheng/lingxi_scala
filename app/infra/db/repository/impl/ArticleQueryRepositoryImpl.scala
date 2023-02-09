package infra.db.repository.impl

import common.{Page, PageQuery}
import domain.article.{Article, ArticleCategory, ArticleTag}
import infra.db.po.ArticlePo.ArticleTable
import infra.db.po.{ArticlePo, ArticleTagTable, CategoryTable, TagTable}
import infra.db.repository.ArticleQueryRepository
import interfaces.dto.ArticlePageQuery
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

  private val articles    = TableQuery[ArticleTable]
  private val tags        = TableQuery[TagTable]
  private val categories  = TableQuery[CategoryTable]
  private val articleTags = TableQuery[ArticleTagTable]

  val queryArticleAction = (id: Long) =>
    articles.filter(table => Seq(table.id === id, table.status =!= Article.Status.DELETE).reduce(_ && _))

  override def get(id: Long): Future[Option[ArticlePo]] = db.run(queryArticleAction(id).result.headOption)

  override def list(): Future[Seq[ArticlePo]] = ???

  override def count(): Future[Int] = ???

  override def listByPage(pageQuery: PageQuery): Future[Page[ArticlePo]] = ???

  override def listTagsById(tagIds: Seq[Long]): Future[Seq[ArticleTag]] = db.run(tags.filter(_.id inSet tagIds.toSet).result)

  override def getCategoryById(categoryId: Long): Future[Option[ArticleCategory]] =
    db.run(categories.filter(_.id === categoryId).result.headOption)

  override def listTags(): Future[Seq[ArticleTag]] = db.run(tags.result)

  override def listCategorises(): Future[Seq[ArticleCategory]] = db.run(categories.result)

  override def getTagByName(name: String): Future[Option[ArticleTag]] = db.run(tags.filter(_.name === name).result.headOption)

  override def getCategoryByName(name: String): Future[Option[ArticleCategory]] =
    db.run(categories.filter(_.name === name).result.headOption)

  override def getArticleTagMap(articleIds: Seq[Long]): Future[Map[Long, Seq[ArticleTag]]] =
    for {
      articleTags <- db.run(articleTags.filter(_.articleId inSet articleIds).result)
      articleTagMap = articleTags.groupMap(_._2)(_._3)
      tagMap <- db.run(tags.filter(_.id inSet articleTags.map(_._3)).result).map(tags => tags.map(tag => tag.id -> tag).toMap)
      result = articleTagMap.map(item => item._1 -> item._2.map(tagId => tagMap(tagId)))
    } yield result

  override def listTagsByArticle(articleId: Long): Future[Seq[ArticleTag]] = {
    val joinQuery = tags join articleTags on (_.id === _.tagId)
    db.run(joinQuery.filter(_._2.articleId === articleId).map(_._1).result)
  }

  override def listCategoryByIds(ids: Seq[Long]): Future[Seq[ArticleCategory]] = db.run(categories.filter(_.id inSet ids).result)

  override def listArticleByPage(query: ArticlePageQuery): Future[Page[ArticlePo]] = {
    val filteredQuery = (articles join articleTags on (_.id === _.articleId))
      .filterOpt(query.tag)(_._2.tagId === _)
      .filterOpt(query.category)(_._1.category === _)
      .filterOpt(query.searchTitle)((e, v) => e._1.title like s"%$v%")

    db.run {
      filteredQuery
        .drop(query.offset)
        .take(query.limit)
        .map(_._1)
        .map(ArticlePo.selectFields)
        .result
        .map(articlePos => Page(query.page, query.size, articlePos.length, articlePos.map(ArticlePo.briefConvert)))
    }

  }

}
