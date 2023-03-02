package interfaces.controller

import application.command.CommentCommand
import application.service.{CommentQueryService, CommentService}
import common.Results
import infra.auth.{AuthenticationAction, AuthorizationAction}
import interfaces.dto.CommentPageQuery
import play.api.mvc.InjectedController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CommentController @Inject() (
  commentService: CommentService,
  commentQueryService: CommentQueryService,
  authenticationAction: AuthenticationAction,
  authorizationAction: AuthorizationAction
) extends InjectedController {

  def addComment = Action(parse.json[CommentCommand]) async { request =>
    commentService.addComment(request.body)(request).map(_ => Ok).recover(ex => Results.fail(ex))
  }

  def listComment(page: Int, size: Int, resourceId: Long) = Action async { request =>
    val pageQuery = CommentPageQuery(page, size)
    commentQueryService.listRootCommentByPage(pageQuery).map(Results.success(_)).recover(ex => Results.fail(ex))
  }

  def listReplyBypage(page: Int, size: Int, parent: Long) = Action async {
    val pageQuery = CommentPageQuery(page, size, None, parent = Some(parent))
    commentQueryService.listReplyByPage(pageQuery).map(Results.success(_)).recover(ex => Results.fail(ex))
  }

  def deleteComment(id: Long) = authenticationAction andThen authorizationAction async {
    commentService.deleteComment(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

}
