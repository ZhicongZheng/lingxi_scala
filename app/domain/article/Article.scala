package domain.article

import domain.BaseEntity

import java.time.LocalDateTime

final case class Article(
  id: Long,
  // 标题
  title: String,
  // 简介
  introduction: String,
  // 封面
  frontCover: String,
  // 标签
  tags: Seq[ArticleTag] = Nil,
  // 分类
  category: ArticleCategory,
  // markdown 内容
  contentMd: String,
  // html 内容
  contentHtml: String,
  // 浏览次数
  viewCount: Long = 0,
  // 点赞次数
  likeCount: Long = 0,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseEntity
