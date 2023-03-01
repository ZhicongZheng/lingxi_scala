package application.service

import com.github.houbb.sensitive.word.core.SensitiveWordHelper
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
      page <- commentQueryRepository.listByPage(pageQuery)
      parentIds = page.data.filter(_.replyTo == -1).map(_.id)
      replies    <- commentQueryRepository.listReplyWithLimit(parentIds, Some(3))
      replyCount <- commentQueryRepository.replyCountMap(parentIds)
    } yield page.map(CommentDto.fromPo).map { dto =>
      val replyList = replies.getOrElse(dto.id, Seq.empty)
      dto.copy(
        reply = replyList.map(po => po.copy(content = SensitiveWordHelper.replace(po.content))).map(CommentDto.fromPo),
        replyCount = replyCount.getOrElse(dto.id, 0),
        content = SensitiveWordHelper.replace(dto.content)
      )
    }

  def listReplyByPage(pageQuery: CommentPageQuery): Future[Page[CommentDto]] =
    for {
      page       <- commentQueryRepository.listByPage(pageQuery)
      replyCount <- commentQueryRepository.replyCountMap(pageQuery.parent.toSeq)
    } yield page.map(CommentDto.fromPo).map { dto =>
      dto.copy(replyCount = replyCount.getOrElse(dto.id, 0), content = SensitiveWordHelper.replace(dto.content))
    }

}
