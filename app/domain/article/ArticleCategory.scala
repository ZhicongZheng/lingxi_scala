package domain.article

import java.time.LocalDateTime

final case class ArticleCategory(id: Long, name: String, parent: Long = -1, createAt: LocalDateTime = LocalDateTime.now())
