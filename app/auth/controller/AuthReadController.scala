package auth.controller

import auth.application.AuthQueryService
import auth.application.dto.UserDto
import common.{Constant, PageQuery, Results}
import common.actions.{AuthorizationAction, UserAction}
import play.api.mvc.{ControllerComponents, InjectedController, Session}

import java.util.concurrent.ThreadLocalRandom
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuthReadController @Inject() (
  override val controllerComponents: ControllerComponents,
  authQueryService: AuthQueryService,
  userAction: UserAction,
  authorizationAction: AuthorizationAction
) extends InjectedController {

  val random = ThreadLocalRandom.current()

  def current = userAction async { implicit request =>
    val currentUser = request.user
    authQueryService
      .richUser(currentUser)
      .map(u => Results.success(UserDto.fromDo(u)))
      .recover(ex => Results.fail(ex))
  }

  def loginCode = Action { implicit request =>
    val code = random.nextInt(1000, 10000).toString
    Ok(code).withSession(Session(Map(Constant.loginCode -> code)))
  }

  def listUserByPage(page: Int, size: Int, sort: Option[String] = None) = userAction andThen authorizationAction async {
    val pageQuery = PageQuery(page, size, sort)
    authQueryService.listUserByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
  }

  def listRoleByPage(page: Int, size: Int, sort: Option[String] = None) = userAction andThen authorizationAction async {
    val pageQuery = PageQuery(page, size, sort)
    authQueryService.listRolesByPage(pageQuery).map(pageDto => Results.success(pageDto)).recover(ex => Results.fail(ex))
  }

}
