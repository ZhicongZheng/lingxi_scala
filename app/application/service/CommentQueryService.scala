package application.service

import common.Page
import infra.db.repository.CommentQueryRepository
import interfaces.dto.{CommentDto, CommentPageQuery}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CommentQueryService @Inject() (commentQueryRepository: CommentQueryRepository) {

  def listCommentByPage(pageQuery: CommentPageQuery): Future[Page[CommentDto]] =
    for {
      page    <- commentQueryRepository.listByPage(pageQuery)
      replies <- commentQueryRepository.listReply(page.data.filter(_.replyTo == -1).map(_.id), Some(3))
    } yield page.map(CommentDto.fromPo).map { dto =>
      val replyList = replies.getOrElse(dto.id, Seq.empty)
      dto.copy(reply = replyList.map(CommentDto.fromPo))
    }

}
