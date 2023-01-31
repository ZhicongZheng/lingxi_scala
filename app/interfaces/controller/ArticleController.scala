package interfaces.controller

import application.command.{ArticleCategoryCommand, ArticleTagCommand}
import application.service.{ArticleCommandService, ArticleQueryService}
import common.Results
import domain.article.{ArticleCategory, ArticleTag}
import infra.actions.{AuthenticationAction, AuthorizationAction}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.InjectedController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ArticleController @Inject() (
  articleQueryService: ArticleQueryService,
  articleCommandService: ArticleCommandService,
  authenticationAction: AuthenticationAction,
  authorizationAction: AuthorizationAction
) extends InjectedController {

  implicit val tagFormat: OFormat[ArticleTag]           = Json.format[ArticleTag]
  implicit val categoryFormat: OFormat[ArticleCategory] = Json.format[ArticleCategory]

  def listArticleTags = Action async {
    articleQueryService.listTags().map(tags => Results.success(tags)).recover(ex => Results.fail(ex))
  }

  def addArticleTag = authenticationAction(parse.json[ArticleTagCommand]) andThen authorizationAction async { request =>
    articleCommandService
      .addTags(request.body)
      .map {
        case Left(err) => Results.fail(err)
        case _         => Created
      }
      .recover(ex => Results.fail(ex))
  }

  def deleteArticleTag(id: Long) = authenticationAction andThen authorizationAction async {
    articleCommandService.removeTag(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

  def listArticleCategory = Action async {
    articleQueryService.listCategorises().map(categories => Results.success(categories)).recover(ex => Results.fail(ex))
  }

  def addArticleCategory = authenticationAction(parse.json[ArticleCategoryCommand]) andThen authorizationAction async { request =>
    articleCommandService
      .addCategory(request.body)
      .map {
        case Left(err) => Results.fail(err)
        case _         => Created
      }
      .recover(ex => Results.fail(ex))
  }

  def deleteArticleCategory(id: Long) = authenticationAction andThen authorizationAction async {
    articleCommandService.removeCategory(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

}
