package infra.db.repository.impl

import common.Constant
import domain.action.Action
import domain.article.{Article, ArticleCategory, ArticleRepository, ArticleTag}
import infra.db.assembler.ArticleAssembler._
import infra.db.po.ActionPo.ActionTable
import infra.db.po.ArticlePo.ArticleTable
import infra.db.po.{ArticleTagTable, CategoryTable, TagTable}
import infra.db.repository.ArticleQueryRepository
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ArticleRepositoryImpl @Inject() (private val dbConfigProvider: DatabaseConfigProvider, queryRepository: ArticleQueryRepository)(
  implicit ec: ExecutionContext
) extends ArticleRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig: DatabaseConfig[PostgresProfile] = dbConfigProvider.get[PostgresProfile]

  private val articles    = TableQuery[ArticleTable]
  private val actions     = TableQuery[ActionTable]
  private val tags        = TableQuery[TagTable]
  private val categories  = TableQuery[CategoryTable]
  private val articleTags = TableQuery[ArticleTagTable]

  val queryArticleAction = (id: Long) =>
    articles.filter(table => Seq(table.id === id, table.status =!= Article.Status.DELETE).reduce(_ && _))

  override def save(article: Article): Future[Long] =
    article.id match {
      case Constant.domainCreateId => doInsert(article)
      case _                       => doUpdate(article)
    }

  override def get(id: Long): Future[Option[Article]] =
    db.run(queryArticleAction(id).result.headOption) flatMap {
      case None => Future.successful(None)
      case Some(articlePo) =>
        val categoryId = articlePo.category
        val selectTags = queryRepository.listTagsByArticle(articlePo.id)
        val selectCategory =
          if (categoryId.isEmpty) Future.successful(None) else queryRepository.getCategoryById(categoryId.get)

        val selectAction = db.run(actions.filter { a =>
          Seq(a.resourceId === articlePo.id, a.typ inSet Seq(Action.Type.LICK_ARTICLE, Action.Type.VIEW_ARTICLE)).reduce(_ && _)
        }.result)

        val future = for {
          tags     <- selectTags
          category <- selectCategory
          actions  <- selectAction
        } yield (tags, category, actions)

        future.map { tuple =>
          val actionMap = tuple._3.groupBy(_.typ)
          val viewCount = actionMap.get(Action.Type.VIEW_ARTICLE).size
          val likeCount = actionMap.get(Action.Type.LICK_ARTICLE).size
          Some(toDo(articlePo).copy(tags = tuple._1, category = tuple._2, viewCount = viewCount, likeCount = likeCount))
        }
    }

  override def remove(id: Long): Future[Unit] = db.run(queryArticleAction(id).map(_.status).update(Article.Status.DELETE)).map(_ => ())

  private def doInsert(article: Article): Future[Long] =
    db.run(articles returning articles.map(_.id) += article).flatMap { articleId =>
      insertArticleTagRef(articleId, article.tags.map(_.id)).map(_ => articleId)
    }

  private def doUpdate(article: Article): Future[Long] =
    for {
      updateCount <- db.run(queryArticleAction(article.id).update(article.copy(updateAt = LocalDateTime.now()))).map(_ => article.id)
      _           <- if (updateCount > 0) deleteArticleTagRef(article.id) else Future.successful(())
      _           <- if (updateCount > 0) insertArticleTagRef(article.id, article.tags.map(_.id)) else Future.successful(())
    } yield article.id

  override def addTag(tag: ArticleTag): Future[Unit] = db.run(tags += tag).map(_ => ())

  override def removeTag(id: Long): Future[Unit] = db.run(tags.filter(_.id === id).delete).map(_ => ())

  override def addCategory(category: ArticleCategory): Future[Unit] = db.run(categories += category).map(_ => ())

  override def removeCategory(id: Long): Future[Unit] = db.run(categories.filter(_.id === id).delete).map(_ => ())

  private def deleteArticleTagRef(articleId: Long): Future[Unit] = db.run(articleTags.filter(_.articleId === articleId).delete).map(_ => ())

  private def insertArticleTagRef(articleId: Long, tags: Seq[Long]): Future[Unit] = {
    val articleTagRows = tags.map(tag => (articleId, tag))
    db.run(articleTags.map(t => (t.articleId, t.tagId)) ++= articleTagRows).map(_ => ())
  }

}
