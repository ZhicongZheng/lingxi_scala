package infra.actions

import common.{Constant, NO_USER, Results, TOKEN_CHECK_ERROR}
import domain.user.entity.User
import domain.user.repository.UserRepository
import play.api.cache.AsyncCacheApi
import play.api.mvc._

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt

case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

@Singleton
class UserAction @Inject() (parser: BodyParsers.Default, userRepository: UserRepository, cacheApi: AsyncCacheApi)(implicit
  ec: ExecutionContext
) extends ActionBuilder[UserRequest, AnyContent] {

  def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {
    def success(user: User) = block.apply(UserRequest(user, request))

    request.headers
      .get(Constant.userId)
      .map(_.toLong)
      .fold(Future.successful(Results.fail(TOKEN_CHECK_ERROR))) { userId =>
        cacheApi.get[User](userId.toString).flatMap {
          case Some(user) => success(user)
          case None =>
            userRepository
              .get(userId)
              .flatMap {
                case None       => Future.successful(Results.fail(NO_USER))
                case Some(user) => cacheApi.set(userId.toString, user, 10.minutes).flatMap(_ => success(user))
              }
        }
      }
  }

  override def parser: BodyParser[AnyContent] = parser

  override protected def executionContext: ExecutionContext = ec
}
