package application.service

import infra.db.repository.ArticleQueryRepository
import interfaces.dto.SiteInfoDto

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SiteQueryService @Inject() (articleQueryRepository: ArticleQueryRepository) {

  def getSiteInfo(): Future[SiteInfoDto] =
    for {
      articleCount  <- articleQueryRepository.count()
      tagCount      <- articleQueryRepository.tagCount()
      categoryCount <- articleQueryRepository.categoryCount()
    } yield SiteInfoDto(articleCount, tagCount, categoryCount, 10000, SiteInfoDto.siteConfig)
}
