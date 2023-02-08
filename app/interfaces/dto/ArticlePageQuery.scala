package interfaces.dto

import common.PageQuery

case class ArticlePageQuery(
  page: Int,
  size: Int,
  tag: Option[Long] = None,
  category: Option[Long] = None,
  searchTitle: Option[String] = None
) extends PageQuery
