package interfaces.controller

import application.query.{AuthQueryService, UserQueryService}
import common.{Constant, PageQuery, Results}
import domain.user.entity.User
import infra.actions.{AuthorizationAction, UserAction}
import interfaces.dto.UserDto
import play.api.mvc.{ControllerComponents, InjectedController, Session}

import java.util.concurrent.ThreadLocalRandom
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuthReadController @Inject() (
  override val controllerComponents: ControllerComponents,
  authQueryService: AuthQueryService,
  userAction: UserAction,
  authorizationAction: AuthorizationAction,
  userQueryService: UserQueryService
) extends InjectedController {

  val random: ThreadLocalRandom = ThreadLocalRandom.current()

  def current = userAction async { implicit request =>
    val currentUser = request.user
    authQueryService
      .richUser(currentUser)
      .map(u => Results.success(UserDto.fromDo(u)))
      .recover(ex => Results.fail(ex))
  }

  def loginCode = Action { implicit request =>
    val code = User.loginCode
    Ok(code).withSession(Session(Map(Constant.loginCode -> code)))
  }

  def listUserByPage(page: Int, size: Int, sort: Option[String] = None) = userAction andThen authorizationAction async {
    val pageQuery = PageQuery(page, size, sort)
    userQueryService.listUserByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
  }

  def listRoleByPage(page: Int, size: Int, sort: Option[String] = None) = userAction andThen authorizationAction async {
    val pageQuery = PageQuery(page, size, sort)
    authQueryService.listRolesByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
  }

}
