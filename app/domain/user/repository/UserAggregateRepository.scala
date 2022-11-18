package domain.user.repository

import domain.AggregateRepository
import domain.user.entity.User

trait UserAggregateRepository extends AggregateRepository[User]
