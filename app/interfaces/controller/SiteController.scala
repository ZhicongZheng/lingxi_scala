package interfaces.controller

import application.command.ActionCommand
import application.service.{ActionService, SiteQueryService}
import com.google.inject.Inject
import common.Results
import play.api.mvc.InjectedController

import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SiteController @Inject() (
  siteQueryService: SiteQueryService,
  actionService: ActionService
) extends InjectedController {

  def getSiteInfo = Action async {
    siteQueryService.getSiteInfo().map(Results.success(_)).recover(ex => Results.fail(ex))
  }

  def onAction = Action(parse.json[ActionCommand]) async { request =>
    actionService.onAction(request.body)(request).map(_ => Ok).recover(ex => Results.fail(ex))
  }
}
