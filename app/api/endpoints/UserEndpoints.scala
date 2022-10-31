package api.endpoints

import _root_.auth.application.dto.{ChangePasswordRequest, CreateUserRequest, LoginRequest, UserDto}
import common.PageDto
import sttp.model.{HeaderNames, StatusCode}
import sttp.tapir._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.play.jsonBody

object UserEndpoints {

  private val baseUserEndpoint = endpoint.in("users").tag("Users")

  private val baseSecuredUserEndpoint = securedWithBearerEndpoint.in("users").tag("Users")

  def endpoints =
    Seq(
      loginEndpoint,
      logoutEndpoint,
      currentUserEndpoint,
      listByPageEndpoint,
      deleteUserEndpoint,
      createUserEndpoint,
      changePwdEndpoint,
      loginCodeEndpoint
    )

  val loginEndpoint = baseUserEndpoint.post
    .name("login")
    .summary("用户登陆")
    .description("输入用户名和密码，登陆管理后台")
    .in("login")
    .in(jsonBody[LoginRequest])
    .out(statusCode(StatusCode.Ok))
    .out(header[String](HeaderNames.Authorization))

  val logoutEndpoint = baseSecuredUserEndpoint.post
    .name("logout")
    .summary("退出登陆")
    .description("用户退出登陆")
    .in("logout")
    .in(header[String](HeaderNames.Authorization))
    .out(statusCode(StatusCode.Ok))

  val currentUserEndpoint = baseSecuredUserEndpoint.get
    .name("currentUser")
    .summary("当前用户信息")
    .description("获取当前用户信息，包括基本信息/权限/角色等，但是不包括密码")
    .in("current")
    .out(jsonBody[UserDto])

  val listByPageEndpoint = baseSecuredUserEndpoint.get
    .name("listByPage")
    .summary("分页获取用户")
    .description("分页的方式获取用户列表，支持排序")
    .in(query[Int]("page").default(1) / query[Int]("size").default(10) / query[Option[String]]("sort"))
    .out(jsonBody[PageDto[UserDto]])

  val deleteUserEndpoint = baseSecuredUserEndpoint.delete
    .name("deleteUser")
    .summary("删除用户")
    .description("根据id 删除用户")
    .in(path[Int]("id"))
    .out(stringBody)

  val createUserEndpoint = baseSecuredUserEndpoint.post
    .name("createUser")
    .summary("创建用户")
    .description("创建用户")
    .in(jsonBody[CreateUserRequest])
    .out(statusCode(StatusCode.Created))
    .out(jsonBody[Long])

  val changePwdEndpoint = baseSecuredUserEndpoint.put
    .name("changeUserPwd")
    .summary("修改密码")
    .description("当前登陆用户修改密码")
    .in("password")
    .in(jsonBody[ChangePasswordRequest])
    .out(statusCode(StatusCode.Ok))
    .errorOut(jsonBody[ErrorMessage])

  val loginCodeEndpoint = baseUserEndpoint.get
    .name("loginCode")
    .summary("登陆验证码")
    .description("登陆时获取验证码")
    .in("login-code")
    .out(jsonBody[String])

}
