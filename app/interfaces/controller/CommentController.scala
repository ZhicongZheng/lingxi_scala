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

  def listComment(page: Int, size: Int) = Action async { request =>
    val pageQuery = CommentPageQuery(page, size, parent = Some(-1))
    commentQueryService.listCommentByPage(pageQuery).map(res => Results.success(res)).recover(ex => Results.fail(ex))
  }

  def deleteComment(id: Long) = authenticationAction andThen authorizationAction async {
    commentService.deleteComment(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

}
