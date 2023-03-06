package application.service

import application.command.CommentCommand
import common.{ARTICLE_NOT_EXIST, EMAIL_FORMAT_NOT_INCORRECT, Errors, REPLY_TO_NOT_EXIST}
import domain.comment.{Comment, CommentRepository}
import infra.db.repository.{ArticleQueryRepository, CommentQueryRepository}
import infra.mail.{CommentMailBuilder, MailService}
import interfaces.dto.CommentEmailInfo
import play.api.mvc.Request

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

@Singleton
class CommentService @Inject() (
  commentRepository: CommentRepository,
  articleQueryRepository: ArticleQueryRepository,
  commentQueryRepository: CommentQueryRepository,
  mailService: MailService
) {

  def addComment(cmd: CommentCommand)(implicit request: Request[_]): Future[Either[Errors, Unit]] = {
    if (!cmd.validateEmail) {
      return Future.successful(Left(EMAIL_FORMAT_NOT_INCORRECT))
    }
    // 如果是文章评论，校验文章是否存在
    val articleFuture = cmd.typ match {
      case Comment.Type.comment => articleQueryRepository.get(cmd.resourceId)
      case _                    => Future.successful(None)
    }
    // 如果是回复评论，校验回复的评论是否存在
    val replyToFuture = cmd.replyTo match {
      case None          => Future.successful(None)
      case Some(replyId) => commentQueryRepository.get(replyId)
    }
    val comment = CommentCommand.toDo(cmd)
    // 验证数据
    val validate = for {
      articleExist <- articleFuture
      replyToExist <- replyToFuture
    } yield (articleExist, replyToExist)

    // 保存评论
    val saveComment = validate.flatMap {
      // 文章不存在，直接返回
      case (None, _) => Future.successful(Left(ARTICLE_NOT_EXIST))
      // 评论回复Id 没有查询到，返回评论不存在
      case (Some(_), replyToOpt) if cmd.replyTo.nonEmpty && replyToOpt.isEmpty => Future.successful(Left(REPLY_TO_NOT_EXIST))
      case (Some(article), replyToOpt) => commentRepository.save(comment).map(_ => Right((article, replyToOpt)))
    }

    // 保存评论之后异步发送邮件
    saveComment.onComplete {
      case Success(Right((article, replyToOpt))) =>
        val allow = (!comment.isReply && comment.canNotify) || (comment.isReply && replyToOpt.nonEmpty && replyToOpt.get.allowNotify)
        if (allow) {
          val buildEmailInfo = CommentEmailInfo(article, replyToOpt, comment)
          // 开发中先去掉通知
          mailService.send(CommentMailBuilder.build(buildEmailInfo))
        }
      case _ => ()
    }
    saveComment.map(_ => Right(()))
  }

  def deleteComment(id: Long): Future[Unit] = commentRepository.remove(id)

}
