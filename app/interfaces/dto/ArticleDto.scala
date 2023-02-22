package interfaces.dto

import domain.article.{Article, ArticleCategory, ArticleTag}
import infra.db.po.ArticlePo

import java.time.LocalDateTime
import scala.language.implicitConversions

case class ArticleDto(
  id: Long,
  // 标题
  title: String,
  // 简介
  introduction: String,
  // 封面
  frontCover: Option[String] = None,
  // 标签
  tags: Seq[ArticleTag] = Nil,
  // 分类
  category: Option[ArticleCategory] = None,
  // markdown 内容
  contentMd: String = "",
  // html 内容
  contentHtml: String = "",
  // 状态 0：草稿 -1: 删除 1: 发布
  status: Int = 0,
  // 浏览次数
  viewCount: Long = 0,
  // 点赞次数
  likeCount: Long = 0,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
)

object ArticleDto {

  implicit def fromPo(po: ArticlePo): ArticleDto =
    ArticleDto(
      id = po.id,
      title = po.title,
      introduction = po.introduction,
      frontCover = po.frontCover,
      status = po.status,
      category = po.category.map(ArticleCategory.justId),
      viewCount = po.viewCount,
      likeCount = po.likeCount,
      createAt = po.createAt,
      updateAt = po.updateAt,
      contentHtml = po.contentHtml,
      contentMd = po.contentMd
    )

  implicit def fromDo(domain: Article): ArticleDto =
    ArticleDto(
      domain.id,
      domain.title,
      domain.introduction,
      domain.frontCover,
      domain.tags,
      domain.category,
      domain.contentMd,
      domain.contentHtml,
      domain.status,
      domain.viewCount,
      domain.likeCount,
      domain.createAt,
      domain.updateAt
    )
}
