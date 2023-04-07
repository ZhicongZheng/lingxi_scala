package interfaces.controller

import application.command.{ArticleCategoryCommand, ArticleCommand, ArticleTagCommand}
import application.service.{ArticleQueryService, ArticleService}
import common.{Page, Results}
import domain.article.{ArticleCategory, ArticleTag}
import infra.auth.{AuthenticationAction, AuthorizationAction}
import interfaces.dto.{ArticleDto, ArticlePageQuery}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{AnyContent, InjectedController, Request}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ArticleController @Inject() (
  articleQueryService: ArticleQueryService,
  articleCommandService: ArticleService,
  authenticationAction: AuthenticationAction,
  authorizationAction: AuthorizationAction
) extends InjectedController {

  implicit val tagFormat: OFormat[ArticleTag]               = Json.format[ArticleTag]
  implicit val categoryFormat: OFormat[ArticleCategory]     = Json.format[ArticleCategory]
  implicit val format: OFormat[ArticleDto]                  = Json.format[ArticleDto]
  implicit val articlePageFormat: OFormat[Page[ArticleDto]] = Json.format[Page[ArticleDto]]

  def getArticle(id: Long) = Action async {
    articleCommandService
      .getArticle(id)
      .map {
        case Left(err)      => Results.fail(err)
        case Right(article) => Results.success(article)
      }
      .recover(ex => Results.fail(ex))
  }

  def createArticle = authenticationAction(parse.json[ArticleCommand]) andThen authorizationAction async { request =>
    articleCommandService
      .createArticle(request.body)
      .map {
        case Left(err) => Results.fail(err)
        case Right(id) => Created(Json.toJson(id))
      }
      .recover(ex => Results.fail(ex))
  }

  def deleteArticle(id: Long) = authenticationAction andThen authorizationAction async {
    articleCommandService.deleteArticle(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

  def updateArticle = authenticationAction(parse.json[ArticleCommand]) andThen authorizationAction async { request =>
    articleCommandService
      .updateArticle(request.body)
      .map {
        case Left(err) => Results.fail(err)
        case Right(_)  => Ok
      }
      .recover(ex => Results.fail(ex))
  }

  def releaseArticle(id: Long) = authenticationAction andThen authorizationAction async {
    articleCommandService.releaseArticle(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

  def offlineArticle(id: Long) = authenticationAction andThen authorizationAction async {
    articleCommandService.offlineArticle(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

  def likeArticle(id: Long, like: Boolean) = Action async { request: Request[AnyContent] =>
    articleCommandService.likeArticle(id, like).map(_ => Ok).recover(ex => Results.fail(ex))
  }

  def listArticleByPage(page: Int, size: Int, tag: Option[Long] = None, category: Option[Long] = None, searchTitle: Option[String] = None) =
    Action async {
      val pageQuery = ArticlePageQuery(page, size, tag, category, searchTitle)
      articleQueryService.listArticleByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
    }

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

  def updateCategory = authenticationAction(parse.json[ArticleCategoryCommand]) andThen authorizationAction async { request =>
    articleCommandService
      .updateCategory(request.body)
      .map {
        case Left(err) => Results.fail(err)
        case Right(_)  => Ok
      }
      .recover(ex => Results.fail(ex))
  }

  def deleteArticleCategory(id: Long) = authenticationAction andThen authorizationAction async {
    articleCommandService.removeCategory(id).map(_ => Ok).recover(ex => Results.fail(ex))
  }

}
