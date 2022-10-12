package auth.domain

import common.Constant
import common.result.{ Errors, LOGIN_FAILED }
import org.mindrot.jbcrypt.BCrypt
import play.api.mvc.JWTCookieDataCodec

import java.time.LocalDateTime
import scala.util.{ Success, Try }

final case class User(
  id: Long,
  username: String,
  password: String,
  avatar: String,
  nickName: String,
  roles: Option[Seq[Role]] = None,
  permissions: Option[Seq[Permission]] = None,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: Option[LocalDateTime] = None
) extends BaseInfo {

  def login(pwd: String, jwt: JWTCookieDataCodec): Either[Errors, String] =
    Try(BCrypt.checkpw(pwd, password)) match {
      case Success(res) if res => Right(jwt.encode(Map(Constant.userId -> id.toString)))
      case _                   => Left(LOGIN_FAILED)
    }

}
