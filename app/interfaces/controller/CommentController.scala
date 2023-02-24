package interfaces.controller

import application.command.CommentCommand
import application.service.CommentService
import common.Results
import play.api.mvc.InjectedController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CommentController @Inject() (commentService: CommentService) extends InjectedController {

  def addComment = Action(parse.json[CommentCommand]) async { request =>
    commentService.addComment(request.body)(request).map(_ => Ok).recover(ex => Results.fail(ex))
  }

}
