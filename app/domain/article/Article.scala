package domain.article

import domain.BaseEntity
import domain.article.Article.Status

import java.time.LocalDateTime

final case class Article(
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
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseEntity {

  def updateBrief(title: String, introduction: String, frontCover: Option[String]): Article =
    this.copy(title = title, introduction = introduction, frontCover = frontCover, updateAt = LocalDateTime.now(), status = Status.DRAFT)

  def updateContent(contentMd: String, contentHtml: String): Article =
    this.copy(contentMd = contentMd, contentHtml = contentHtml, updateAt = LocalDateTime.now(), status = Status.DRAFT)

  def onView(): Article = this.copy(viewCount = viewCount + 1)

  def onLike(): Article = this.copy(likeCount = likeCount + 1)

  def changeCategory(category: ArticleCategory): Article = this.copy(category = Some(category), updateAt = LocalDateTime.now())

  def changeTags(tags: Seq[ArticleTag]): Article = this.copy(tags = tags, updateAt = LocalDateTime.now())

  def release(): Article = this.copy(status = Status.RELEASE)

}

object Article {

  object Status {
    val DRAFT       = 0
    val DELETE: Int = -1
    val RELEASE     = 1
  }

  def apply(
    id: Long,
    title: String,
    introduction: String,
    frontCover: Option[String],
    contentMd: String,
    contentHtml: String,
    status: Int,
    createBy: Long,
    updateBy: Long,
    createAt: LocalDateTime,
    updateAt: LocalDateTime
  ): Article =
    Article(id, title, introduction, frontCover, Nil, None, contentMd, contentHtml, status, 0, 0, createBy, updateBy, createAt, updateAt)

}
