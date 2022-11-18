package application.service

import common.{Page, PageQuery}
import infra.db.repository.UserQueryRepository
import interfaces.dto.UserDto

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserQueryService @Inject() (
  private val userQueryRepository: UserQueryRepository
) {

  def listUserByPage(pageQuery: PageQuery): Future[Page[UserDto]] = userQueryRepository.listByPage(pageQuery).map(_.map(UserDto.fromPo))

}
