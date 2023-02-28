package interfaces.api.endpoints

import application.command.CommentCommand
import common.Page
import interfaces.dto.CommentDto
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.play._

object CommentEndpoints {

  val withoutAuthEndpoint = endpoint.in("comments").tag("Comments").errorOut(jsonBody[ErrorMessage])

  val authEndpoint = securedWithBearerEndpoint.in("comments").tag("Comments")

  def endpoints = Seq(addCommentEndpoint, listCommentByPageEndpoint, deleteCommentEndpoint, listReplyByPageEndpoint)

  val addCommentEndpoint = withoutAuthEndpoint.post
    .name("addComment")
    .summary("添加一条评论")
    .description("添加一条评论")
    .in(jsonBody[CommentCommand])
    .out(statusCode(StatusCode.Ok))

  val listCommentByPageEndpoint = withoutAuthEndpoint.get
    .name("listCommentByPage")
    .summary("分页获取评论列表")
    .description("分页获取评论列表")
    .in(query[Int]("page").default(1) / query[Int]("size").default(10) / query[Long]("resourceId"))
    .out(jsonBody[Page[CommentDto]])

  val listReplyByPageEndpoint = withoutAuthEndpoint.get
    .name("listReplyByPage")
    .summary("分页获取评论回复列表")
    .description("分页获取评论回复列表")
    .in(query[Int]("page").default(1) / path[Long]("parent") / query[Int]("size").default(10) / "replies")
    .out(jsonBody[Page[CommentDto]])

  val deleteCommentEndpoint = authEndpoint.delete
    .name("deleteComment")
    .summary("删除评论")
    .description("根据id删除评论")
    .in(path[Long]("id"))
    .out(statusCode(StatusCode.Ok))

}
