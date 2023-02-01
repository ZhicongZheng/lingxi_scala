package application.service

import common.{Page, PageQuery}
import domain.article.{ArticleCategory, ArticleTag}
import infra.db.repository.ArticleQueryRepository
import interfaces.dto.ArticleDto

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ArticleQueryService @Inject() (articleQueryRepository: ArticleQueryRepository) {

  def listArticleByPage(pageQuery: PageQuery): Future[Page[ArticleDto]] =
    for {
      // 分页文章列表
      articlePage <- articleQueryRepository.listByPage(pageQuery).map(_.map(ArticleDto.fromPo))
      // 文章分类id 的map
      articleCategoryMap = articlePage.data.map(article => article.id -> article.category).toMap
      // 文章标签map
      tagsMap <- articleQueryRepository.getArticleTagMap(articleCategoryMap.keySet.toSeq)
      // 分类的id
      categoryIds = articleCategoryMap.values.filter(_.isDefined).map(_.get.id).toSeq
      // 文章分类map
      categoryMap <- articleQueryRepository.listCategoryByIds(categoryIds).map(seq => seq.map(category => category.id -> category).toMap)
      pageResult = articlePage.map { dto =>
        dto.category match {
          case Some(category) => dto.copy(category = categoryMap.get(category.id), tags = tagsMap.getOrElse(dto.id, Nil))
          case None           => dto.copy(tags = tagsMap.getOrElse(dto.id, Nil))
        }
      }
    } yield pageResult

  def listTags(): Future[Seq[ArticleTag]] = articleQueryRepository.listTags()

  def listCategorises(): Future[Seq[ArticleCategory]] = articleQueryRepository.listCategorises()

}
