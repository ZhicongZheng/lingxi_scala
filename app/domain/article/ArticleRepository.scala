package domain.article

import domain.AggregateRepository

trait ArticleRepository extends AggregateRepository[Article]
