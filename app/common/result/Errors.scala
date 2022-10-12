package common.result

trait Errors {
  val code: Int

  val message: String
}

case object NO_USER extends Errors { val code = 10000; val message = "用户不存在" }

case object LOGIN_FAILED extends Errors { val code = 10001; val message = "登录失败，请检查用户名或密码！" }

case object TOKEN_CHECK_ERROR extends Errors {val code = 10004; val message = "登录状态校验失败，请重新登录！"}
