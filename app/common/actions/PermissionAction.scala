package common.actions

import auth.domain.User
import play.api.mvc.{Request, WrappedRequest}

case class PermissionRequest[A <: User](user: User, request: Request[A]) extends WrappedRequest(request)

class PermissionAction {

}
