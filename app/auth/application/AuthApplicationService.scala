package auth.application

import auth.application.dto.LoginRequest
import auth.domain.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuthApplicationService @Inject() (val userRepository: UserRepository) {

  def login(loginRequest: LoginRequest): Future[String] = {
    userRepository.findByUsername(loginRequest.username).flatMap {
      case Some(user) => user.login(loginRequest.password)
      case None => Future.failed(new IllegalAccessException())
    }

    val userOptFuture = userRepository.findByUsername(loginRequest.username)
    userOptFuture
    Future.successful("")
  }

}
