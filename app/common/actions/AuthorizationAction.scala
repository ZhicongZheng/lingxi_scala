package common.actions

import auth.domain.repository.{PermissionRepository, RoleRepository, UserRepository}
import common.Constant
import common.actions.AuthorizedRequest.{failureResult, parsePermission}
import common.result.PERMISSION_DENIED
import common.Constant.superAdmin
import play.api.mvc.{ActionRefiner, BodyParsers, Result, WrappedRequest}
import play.api.routing.{HandlerDef, Router}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

case class AuthorizedRequest[A](request: UserRequest[A]) extends WrappedRequest(request)

@Singleton
class AuthorizationAction @Inject() (
  parser: BodyParsers.Default,
  userRepository: UserRepository,
  roleRepository: RoleRepository,
  permissionRepository: PermissionRepository
)(implicit ec: ExecutionContext)
    extends ActionRefiner[UserRequest, AuthorizedRequest] {

  override protected def executionContext: ExecutionContext = ec

  override protected def refine[A](userRequest: UserRequest[A]): Future[Either[Result, AuthorizedRequest[A]]] = {
    val user = userRequest.user
    for {
      role        <- roleRepository.findByUserId(user.id)
      permissions <- if (role.isEmpty) Future.successful(Nil) else permissionRepository.findByRoleId(role.get.id)
    } yield {
      val handlerDef        = userRequest.request.attrs.get(Router.Attrs.HandlerDef)
      val permission        = parsePermission(handlerDef)
      def authorizedRequest = AuthorizedRequest(userRequest.copy(user = user.copy(role = role, permissions = permissions)))
      role match {
        case None => Left(failureResult)
        // 超级管理员不校验权限
        case Some(role) if role.code == superAdmin => Right(authorizedRequest)
        case Some(_) =>
          permissions.filter(_.`type` == Constant.functionPermission).filter(_.value == permission) match {
            case Nil => Left(failureResult)
            case _   => Right(authorizedRequest)
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
