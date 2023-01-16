package domain.article

import java.time.LocalDateTime

final case class ArticleTag(id: Long, name: String, alias: String, createAt: LocalDateTime)
