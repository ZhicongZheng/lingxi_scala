package infra.actions

import common.{Constant, PERMISSION_DENIED}
import common.Constant.superAdmin
import infra.actions.AuthorizationAction.{failureResult, parsePermission}
import play.api.mvc.{ActionRefiner, Result}
import play.api.routing.{HandlerDef, Router}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthorizationAction @Inject() (implicit ec: ExecutionContext) extends ActionRefiner[UserRequest, UserRequest] {

  override protected def executionContext: ExecutionContext = ec

  override protected def refine[A](userRequest: UserRequest[A]): Future[Either[Result, UserRequest[A]]] = {
    val user = userRequest.user

    val handlerDef        = userRequest.request.attrs.get(Router.Attrs.HandlerDef)
    val requirePermission = parsePermission(handlerDef)

    Future {
      user.role match {
        case None => Left(failureResult)
        // 超级管理员不校验权限
        case Some(role) if role.code == superAdmin => Right(userRequest)
        case Some(role) =>
          role.permissions.filter(_.`type` == Constant.functionPermission).filter(_.value == requirePermission) match {
            case Nil => Left(failureResult)
            case _   => Right(userRequest)
          }
      }
    }
  }

}

object AuthorizationAction {

  def parsePermission(handlerDefOpt: Option[HandlerDef]): String =
    handlerDefOpt.map(handlerDef => s"${handlerDef.controller}.${handlerDef.method}").getOrElse("")

  val failureResult: Result = common.Results.fail(PERMISSION_DENIED)
}
