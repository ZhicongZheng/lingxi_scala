package application.service

import application.command.CreateArticleCommand
import application.command.CreateArticleCommand.toDo
import com.google.inject.Inject
import common.{CATEGORY_EXIST, Errors, TAG_EXIST, TAG_OR_CATEGORY_NOT_EXIST}
import domain.article.{ArticleCategory, ArticleRepository, ArticleTag}
import infra.db.repository.ArticleQueryRepository

import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ArticleCommandService @Inject() (articleRepository: ArticleRepository, articleQueryRepository: ArticleQueryRepository) {

  def createArticle(command: CreateArticleCommand): Future[Either[Errors, Long]] = {

    val categoryFuture = command.category match {
      case Some(id) => articleQueryRepository.getCategoryById(id)
      case None     => Future.successful[Option[ArticleCategory]](None)
    }

    val saveArticle = (tags: Seq[ArticleTag], category: Option[ArticleCategory]) =>
      if (tags.size == command.tags.size && category.exists(_.id == command.category.get)) {
        articleRepository.save(toDo(command).copy(tags = tags, category = category)).map(Right(_))
      } else Future.successful(Left(TAG_OR_CATEGORY_NOT_EXIST))

    for {
      tags     <- articleQueryRepository.listTagsById(command.tags)
      category <- categoryFuture
      result   <- saveArticle(tags, category)
    } yield result
  }

  def addTags(tag: ArticleTag): Future[Either[Errors, Unit]] =
    for {
      existTag <- articleQueryRepository.getTagByName(tag.name)
      result   <- if (existTag.isDefined) Future.successful(Left(TAG_EXIST)) else articleRepository.addTag(tag).map(_ => Right(()))
    } yield result

  def removeTag(id: Long): Future[Unit] = articleRepository.removeTag(id)

  def addCategory(category: ArticleCategory): Future[Either[Errors, Unit]] =
    for {
      existCategory <- articleQueryRepository.getCategoryByName(category.name)
      result <-
        if (existCategory.isDefined) Future.successful(Left(CATEGORY_EXIST))
        else articleRepository.addCategory(category).map(_ => Right(()))
    } yield result

  def removeCategory(id: Long): Future[Unit] = articleRepository.removeCategory(id)

}
