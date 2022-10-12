package auth.controller

import auth.application.AuthApplicationService
import auth.application.dto.LoginRequest
import common.Results
import play.api.http.HeaderNames
import play.api.mvc._

import javax.inject.{ Inject, Singleton }
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuthWriteController @Inject() (
  override val controllerComponents: ControllerComponents,
  authApplicationService: AuthApplicationService
) extends InjectedController {

  def login = Action(parse.json[LoginRequest]).async { request =>
    authApplicationService
      .login(request.body)
      .map {
        case Left(error)  => Results.fail(error)
        case Right(token) => Ok.withHeaders((HeaderNames.AUTHORIZATION, token))
      }
      .recover(ex => Results.fail(ex))
  }
}
