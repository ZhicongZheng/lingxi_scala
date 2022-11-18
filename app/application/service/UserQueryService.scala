package application.service

import common.{Page, PageQuery}
import domain.user.repository.UserRepository
import interfaces.dto.UserDto

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserQueryService @Inject() (
  private val userRepository: UserRepository
) {

  def listUserByPage(pageQuery: PageQuery): Future[Page[UserDto]] = userRepository.listByPage(pageQuery).map(_.map(UserDto.fromDo))

}
