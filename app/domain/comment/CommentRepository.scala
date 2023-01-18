package domain.comment

import domain.AggregateRepository

trait CommentRepository extends AggregateRepository[Comment] {}
