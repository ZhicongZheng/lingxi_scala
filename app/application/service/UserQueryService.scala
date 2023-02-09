package application.service

import common.{BasePageQuery, Page}
import infra.db.repository.{RoleQueryRepository, UserQueryRepository}
import interfaces.dto.UserDto

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserQueryService @Inject() (
  private val userQueryRepository: UserQueryRepository,
  private val roleQueryRepository: RoleQueryRepository
) {

  def listUserByPage(pageQuery: BasePageQuery): Future[Page[UserDto]] =
    for {
      userPage <- userQueryRepository.listByPage(pageQuery).map(_.map(UserDto.fromPo))
      users = userPage.data
      userRoleMap <- roleQueryRepository.findUserRoleMap(users.map(_.id))
      result = userPage.copy(data = users.map(user => user.copy(role = Some(userRoleMap(user.id)))))
    } yield result

}
