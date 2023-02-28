package application.service

import application.command.CommentCommand
import common.{ARTICLE_NOT_EXIST, Errors}
import domain.comment.{Comment, CommentRepository}
import infra.db.repository.ArticleQueryRepository
import play.api.mvc.Request

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class CommentService @Inject() (commentRepository: CommentRepository, articleQueryRepository: ArticleQueryRepository) {

  def addComment(cmd: CommentCommand)(implicit request: Request[_]): Future[Either[Errors, Unit]] =
    // 如果是文章评论，校验文章是否存在
    for {
      articleExist <- cmd.typ match {
        case Comment.Type.comment => articleQueryRepository.get(cmd.resourceId).map(opt => opt.nonEmpty)
        case _                    => Future.successful(false)
      }
      res <- if (articleExist) commentRepository.save(cmd).map(_ => Right(())) else Future.successful(Left(ARTICLE_NOT_EXIST))
    } yield res

  def deleteComment(id: Long): Future[Unit] = commentRepository.remove(id)

}
