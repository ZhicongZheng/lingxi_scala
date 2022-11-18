package infra.actions

import common.{Constant, PERMISSION_DENIED}
import common.Constant.superAdmin
import infra.actions.AuthorizedRequest.{failureResult, parsePermission}
import play.api.mvc.{ActionRefiner, BodyParsers, Result, WrappedRequest}
import play.api.routing.{HandlerDef, Router}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

case class AuthorizedRequest[A](request: UserRequest[A]) extends WrappedRequest(request)

@Singleton
class AuthorizationAction @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext)
    extends ActionRefiner[UserRequest, AuthorizedRequest] {

  override protected def executionContext: ExecutionContext = ec

  override protected def refine[A](userRequest: UserRequest[A]): Future[Either[Result, AuthorizedRequest[A]]] = {
    val user = userRequest.user

    val handlerDef        = userRequest.request.attrs.get(Router.Attrs.HandlerDef)
    val requirePermission = parsePermission(handlerDef)

    Future {
      user.role match {
        case None => Left(failureResult)
        // 超级管理员不校验权限
        case Some(role) if role.code == superAdmin => Right(AuthorizedRequest(userRequest))
        case Some(role) =>
          role.permissions.filter(_.`type` == Constant.functionPermission).filter(_.value == requirePermission) match {
            case Nil => Left(failureResult)
            case _   => Right(AuthorizedRequest(userRequest))
          }
      }
    }
  }

}

object AuthorizedRequest {

  def parsePermission(handlerDefOpt: Option[HandlerDef]): String =
    handlerDefOpt.map(handlerDef => s"${handlerDef.controller}.${handlerDef.method}").getOrElse("")

  val failureResult: Result = common.Results.fail(PERMISSION_DENIED)
}
