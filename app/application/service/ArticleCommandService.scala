package application.service

import application.command.ArticleCommand
import application.command.ArticleCommand.toDo
import com.google.inject.Inject
import common.{ARTICLE_NOT_EXIST, CATEGORY_EXIST, Constant, Errors, TAG_EXIST, TAG_OR_CATEGORY_NOT_EXIST}
import domain.article.{ArticleCategory, ArticleRepository, ArticleTag}
import infra.db.repository.ArticleQueryRepository

import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ArticleCommandService @Inject() (articleRepository: ArticleRepository, articleQueryRepository: ArticleQueryRepository) {

  def createArticle(command: ArticleCommand): Future[Either[Errors, Long]] = {

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

  def updateArticle(command: ArticleCommand): Future[Either[Errors, Long]] =
    // 如果没传id, 默认按照 -1 查找，返回 文章不存在
    articleRepository.get(command.id.getOrElse(Constant.domainCreateId)).flatMap {
      case None => Future.successful(Left(ARTICLE_NOT_EXIST))
      case Some(article) =>
        val updatedArticle = article
          .updateBrief(command.title, command.introduction, command.frontCover)
          .updateContent(command.contentMd, command.contentHtml)
        articleRepository.save(updatedArticle).map(id => Right(id))
    }

  def releaseArticle(id: Long): Future[Either[Errors, Unit]] =
    articleRepository.get(id).flatMap {
      case None => Future.successful(Left(ARTICLE_NOT_EXIST))
      case Some(article) =>
        articleRepository.save(article.release()).map(_ => Right(()))
    }

  def deleteArticle(id: Long): Future[Unit] = articleRepository.remove(id)

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
