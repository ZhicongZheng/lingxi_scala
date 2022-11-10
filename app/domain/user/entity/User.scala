package domain.user.entity

import common.{Constant, Errors, LOGIN_FAILED}
import domain.BaseEntity
import domain.auth.entity.{Permission, Role}
import org.mindrot.jbcrypt.BCrypt
import play.api.mvc.JWTCookieDataCodec

import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom
import scala.util.{Success, Try}

final case class User(
  id: Long,
  username: String,
  password: String,
  avatar: String,
  nickName: String,
  phone: String,
  email: String,
  role: Option[Role] = None,
  permissions: Seq[Permission] = Nil,
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseEntity {

  def login(pwd: String, jwt: JWTCookieDataCodec): Either[Errors, String] =
    Try(BCrypt.checkpw(pwd, password)) match {
      case Success(res) if res => Right(jwt.encode(Map(Constant.userId -> id.toString)))
      case _                   => Left(LOGIN_FAILED)
    }

  def checkPwd(oldPassword: String): Boolean = Try(BCrypt.checkpw(oldPassword, password)).getOrElse(false)

}

object User {

  val random: ThreadLocalRandom = ThreadLocalRandom.current()

  def loginCode: String = random.nextInt(1000, 10000).toString

  def entryPwd(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
}
