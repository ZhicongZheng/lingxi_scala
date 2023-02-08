package interfaces.api.endpoints

import application.command.{ArticleCommand, ArticleTagCommand}
import common.Page
import domain.article.{ArticleCategory, ArticleTag}
import interfaces.dto.ArticleDto
import play.api.libs.json.{Json, OFormat}
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.play._

object ArticleEndpoints {

  implicit val tagFormat: OFormat[ArticleTag]               = Json.format[ArticleTag]
  implicit val categoryFormat: OFormat[ArticleCategory]     = Json.format[ArticleCategory]
  implicit val format: OFormat[ArticleDto]                  = Json.format[ArticleDto]
  implicit val articlePageFormat: OFormat[Page[ArticleDto]] = Json.format[Page[ArticleDto]]

  val tagWithoutAuthEndpoint = endpoint.in("tags").tag("Article Tags").errorOut(jsonBody[ErrorMessage])

  val tagAuthEndpoint = securedWithBearerEndpoint.in("tags").tag("Article Tags")

  val categoryWithoutAuthEndpoint = endpoint.in("categories").tag("Article Categories").errorOut(jsonBody[ErrorMessage])

  val categoryAuthEndpoint = securedWithBearerEndpoint.in("categories").tag("Article Categories")

  val articleWithoutAuthEndpoint = endpoint.in("articles").tag("Article").errorOut(jsonBody[ErrorMessage])

  val articleAuthEndpoint = securedWithBearerEndpoint.in("articles").tag("Article")

  def endpoints =
    Seq(
      listTagsEndpoint,
      addTagsEndpoing,
      deleteTagsEndpoint,
      listCategoryEndpoint,
      addCategoryEndpoing,
      deleteCategoryEndpoint,
      createArticleEndpoint,
      deleteArticleEndpoint,
      updateArticleEndpoint,
      listArticleByPageEndpoint,
      releaseArticleEndpoint
    )

  val createArticleEndpoint = articleAuthEndpoint.post
    .name("createArticle")
    .summary("创建文章")
    .description("创建文章")
    .in(jsonBody[ArticleCommand])
    .out(statusCode(StatusCode.Created))
    .out(jsonBody[Long])

  val updateArticleEndpoint = articleAuthEndpoint.put
    .name("updateArticle")
    .summary("更新文章")
    .description("更新文章")
    .in(jsonBody[ArticleCommand])
    .out(statusCode(StatusCode.Ok))

  val deleteArticleEndpoint = articleAuthEndpoint.delete
    .name("deleteArticle")
    .summary("删除文章")
    .description("删除文章")
    .in(path[Long]("id"))
    .out(statusCode(StatusCode.Ok))

  val releaseArticleEndpoint = articleAuthEndpoint.post
    .name("releaseArticle")
    .summary("发布文章")
    .description("发布文章")
    .in("release")
    .in(path[Long]("id"))
    .out(statusCode(StatusCode.Ok))

  val listArticleByPageEndpoint = articleWithoutAuthEndpoint.get
    .name("listArticleByPage")
    .summary("分页获取文章")
    .description("分页获取文章列表")
    .in(
      query[Int]("page").default(1) / query[Int]("size").default(10)
        / query[Option[Long]]("tag").default(None) / query[Option[Long]]("category").default(None)
        / query[Option[String]]("searchTitle").default(None)
    )
    .out(jsonBody[Page[ArticleDto]])

  val listTagsEndpoint = tagWithoutAuthEndpoint.get
    .name("listTags")
    .summary("获取文章标签列表")
    .description("获取文章标签列表")
    .out(jsonBody[Seq[ArticleTag]])
    .out(statusCode(StatusCode.Ok))

  val addTagsEndpoing = tagAuthEndpoint.post
    .name("addTags")
    .summary("增加文章标签")
    .description("增加文章标签")
    .in(jsonBody[ArticleTagCommand])
    .out(statusCode(StatusCode.Created))

  val deleteTagsEndpoint = tagAuthEndpoint.delete
    .name("deleteTags")
    .summary("删除文章标签")
    .description("删除文章标签")
    .in(path[Long]("id"))
    .out(statusCode(StatusCode.Ok))

  val listCategoryEndpoint = categoryWithoutAuthEndpoint.get
    .name("listCategories")
    .summary("获取文章分类列表")
    .description("获取分类列表")
    .out(jsonBody[Seq[ArticleTag]])
    .out(statusCode(StatusCode.Ok))

  val addCategoryEndpoing = categoryAuthEndpoint.post
    .name("addCategory")
    .summary("增加文章分类")
    .description("增加文章分类")
    .in(jsonBody[ArticleTagCommand])
    .out(statusCode(StatusCode.Created))

  val deleteCategoryEndpoint = categoryAuthEndpoint.delete
    .name("deleteCategory")
    .summary("删除文章分类")
    .description("删除文章分类")
    .in(path[Long]("id"))
    .out(statusCode(StatusCode.Ok))

}
