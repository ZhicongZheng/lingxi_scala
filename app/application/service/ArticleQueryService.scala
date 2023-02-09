package application.service

import common.Page
import domain.article.ArticleTag
import infra.db.repository.ArticleQueryRepository
import interfaces.dto.{ArticleCategoryDto, ArticleDto, ArticlePageQuery}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ArticleQueryService @Inject() (articleQueryRepository: ArticleQueryRepository) {

  def listArticleByPage(pageQuery: ArticlePageQuery): Future[Page[ArticleDto]] =
    articleQueryRepository.listArticleByPage(pageQuery).map(_.map(ArticleDto.fromPo)).flatMap { articlePage =>
      // 文章分类id 的map
      val articleCategoryMap = articlePage.data.map(article => article.id -> article.category).toMap
      val tagsMapFuture      = articleQueryRepository.getArticleTagMap(articleCategoryMap.keySet.toSeq)
      // 分类的id
      val categoryIds = articleCategoryMap.values.filter(_.isDefined).map(_.get.id).toSeq
      val categoryMapFuture =
        articleQueryRepository.listCategoryByIds(categoryIds).map(seq => seq.map(category => category.id -> category).toMap)
      for {
        // 文章标签map
        tagsMap <- tagsMapFuture
        // 文章分类map
        categoryMap <- categoryMapFuture
        pageResult = articlePage.map { dto =>
          dto.category match {
            case Some(category) => dto.copy(category = categoryMap.get(category.id), tags = tagsMap.getOrElse(dto.id, Nil))
            case None           => dto.copy(tags = tagsMap.getOrElse(dto.id, Nil))
          }
        }
      } yield pageResult
    }

  def listTags(): Future[Seq[ArticleTag]] = articleQueryRepository.listTags()

  def listCategorises(): Future[Seq[ArticleCategoryDto]] =
    for {
      categories <- articleQueryRepository.listCategorises().map(seq => seq.map(ArticleCategoryDto.fromPo))
      result = categories.map(c => c.copy(children = categories.filter(_.parent == c.id)))
    } yield result

}
