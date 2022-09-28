package auth.controller

import auth.application.AuthApplicationService
import auth.application.dto.LoginRequest
import common.ResultHelper
import common.exceptions.BizException
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, ControllerComponents, InjectedController, Request}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuthWriteController @Inject()(override val controllerComponents: ControllerComponents,
                                    authApplicationService: AuthApplicationService)
  extends InjectedController {

  def login = Action.async { implicit  request: Request[AnyContent] =>
    val loginRequest = Json.fromJson[LoginRequest](request.body.asJson.get)
    authApplicationService.login(loginRequest.get).map { token =>
      Ok(ResultHelper.success(None)).withHeaders((HeaderNames.AUTHORIZATION, token))
    }.recover { case ex: BizException =>
      Ok(ResultHelper.fail(ex))
    }
  }

}
