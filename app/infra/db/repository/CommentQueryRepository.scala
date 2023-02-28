package infra.db.repository

import common.{Page, PageQuery}
import infra.db.po.CommentsPo

import scala.concurrent.Future

trait CommentQueryRepository extends QueryRepository[CommentsPo] {

  def listReplyWithLimit(rootCommentIds: Seq[Long], limit: Option[Int]): Future[Map[Long, Seq[CommentsPo]]]

  def listReplyByPage(pageQuery: PageQuery, parent: Long): Future[Page[CommentsPo]]

  def replyCountMap(ids: Seq[Long]): Future[Map[Long, Int]]

}
