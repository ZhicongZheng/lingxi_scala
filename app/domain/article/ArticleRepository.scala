package domain.article

import domain.AggregateRepository

import scala.concurrent.Future

trait ArticleRepository extends AggregateRepository[Article] {


}
