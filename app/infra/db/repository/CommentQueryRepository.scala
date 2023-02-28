package infra.db.repository

import common.{Page, PageQuery}
import infra.db.po.CommentsPo

import scala.concurrent.Future

trait CommentQueryRepository extends QueryRepository[CommentsPo] {

  def listReply(rootCommentIds: Seq[Long], limit: Option[Int]): Future[Map[Long, Seq[CommentsPo]]]

  def listReplyByPage(pageQuery: PageQuery, parent: Long): Future[Page[CommentsPo]]

}
