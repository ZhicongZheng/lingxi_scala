package auth.controller

import auth.application.dto.LoginRequest
import play.api.mvc.{ControllerComponents, InjectedController}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class AuthWriteController @Inject()(override val controllerComponents: ControllerComponents) extends InjectedController {

  def login(loginRequest: LoginRequest) = Action.async {
    Future.successful(Ok)
  }

}
