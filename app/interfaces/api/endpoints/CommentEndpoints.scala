package interfaces.api.endpoints

import application.command.CommentCommand
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.play._

object CommentEndpoints {

  val withoutAuthEndpoint = endpoint.in("comments").tag("Comments").errorOut(jsonBody[ErrorMessage])

  def endpoints = Seq(addCommentEndpoint)

  val addCommentEndpoint = withoutAuthEndpoint.post
    .name("addComment")
    .summary("添加一条评论")
    .description("添加一条评论")
    .in(jsonBody[CommentCommand])
    .out(statusCode(StatusCode.Ok))

}
