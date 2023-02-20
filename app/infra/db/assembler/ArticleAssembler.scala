package infra.db.assembler

import domain.action.Action
import domain.article.Article
import infra.db.po.{ActionPo, ArticlePo}

import scala.language.implicitConversions

object ArticleAssembler {

  implicit def fromDo(a: Article): ArticlePo = ArticlePo(
    a.id,
    a.title,
    a.introduction,
    a.frontCover,
    a.contentMd,
    a.contentHtml,
    a.status,
    a.category.map(_.id),
    a.viewCount,
    a.likeCount,
    a.createBy,
    a.updateBy,
    a.createAt,
    a.updateAt
  )

  implicit def toDo(a: ArticlePo): Article = Article(
    a.id,
    a.title,
    a.introduction,
    a.frontCover,
    a.contentMd,
    a.contentHtml,
    a.status,
    a.viewCount,
    a.likeCount,
    a.createBy,
    a.updateBy,
    a.createAt,
    a.updateAt
  )

  implicit def toDo(po: ActionPo): Action =
    Action(po.id, po.typ, po.resourceId, po.remoteAddress, po.createBy, po.updateBy, po.createAt, po.updateAt)

  implicit def toDoOpt(opt: Option[ArticlePo]): Option[Article] = opt.map(toDo)

}
