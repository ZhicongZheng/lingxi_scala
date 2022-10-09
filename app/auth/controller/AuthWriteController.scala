package auth.controller

import auth.application.AuthApplicationService
import auth.application.dto.LoginRequest
import common.ResultHelper
import common.exceptions.BizException
import play.api.http.HeaderNames
import play.api.mvc._

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuthWriteController @Inject()(override val controllerComponents: ControllerComponents,
                                    authApplicationService: AuthApplicationService)
  extends InjectedController {

  def login = Action(parse.json[LoginRequest]).async { request =>
    authApplicationService.login(request.body).map { token =>
      Ok(ResultHelper.success()).withHeaders((HeaderNames.AUTHORIZATION, token))
    }.recover { case ex: BizException =>
      Ok(ResultHelper.fail(ex.getResponseCode))
    }
  }
}
