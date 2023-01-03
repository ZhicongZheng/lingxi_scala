package application.service

import common.{Page, PageQuery}
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

  def listUserByPage(pageQuery: PageQuery): Future[Page[UserDto]] =
    userQueryRepository.listByPage(pageQuery).map(_.map(UserDto.fromPo)).flatMap { userPage =>
      val users   = userPage.data
      val userIds = users.map(_.id)

      roleQueryRepository.findUserRoleMap(userIds).map { userRoleMap =>
        userPage.copy(data = users.map(user => user.copy(role = Some(userRoleMap(user.id)))))
      }
    }

}
