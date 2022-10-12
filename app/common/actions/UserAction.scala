package common.actions

import auth.domain.{ User, UserRepository }
import common.result.{ NO_USER, TOKEN_CHECK_ERROR }
import common.{ Constant, ResultHelper }
import play.api.mvc.Results.Ok
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ ExecutionContext, Future }

case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

class UserAction @Inject() (parser: BodyParsers.Default, userRepository: UserRepository)(implicit ec: ExecutionContext)
    extends ActionBuilder[UserRequest, AnyContent] {

  def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] =
    request.headers.get(Constant.userId).map(_.toLong) match {
      case Some(userId) =>
        userRepository.findById(userId).flatMap {
          case Some(user) => block.apply(UserRequest(user, request))
          case None       => Future.successful(Ok(ResultHelper.fail(NO_USER)))
        }
      case None => Future.successful(Ok(ResultHelper.fail(TOKEN_CHECK_ERROR)))
    }

  override def parser: BodyParser[AnyContent] = parser

  override protected def executionContext: ExecutionContext = ec
}
