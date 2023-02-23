package domain.user

import common.{Errors, LOGIN_FAILED}
import domain.BaseEntity
import domain.auth.Role
import domain.user.User.entryPwd
import org.mindrot.jbcrypt.BCrypt
import play.api.libs.json.{Json, OFormat}

import java.time.LocalDateTime
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
  createBy: Long = 0L,
  updateBy: Long = 0L,
  createAt: LocalDateTime = LocalDateTime.now(),
  updateAt: LocalDateTime = LocalDateTime.now()
) extends BaseEntity {

  def login(pwd: String): Either[Errors, User] =
    Try(BCrypt.checkpw(pwd, password)) match {
      case Success(res) if res => Right(this)
      case _                   => Left(LOGIN_FAILED)
    }

  def checkPwd(oldPassword: String): Boolean = Try(BCrypt.checkpw(oldPassword, password)).getOrElse(false)

  def changeRole(role: Role): User = copy(role = Some(role))

  def changePwd(password: String): User = copy(password = entryPwd(password))

  def update(param: (String, String, String, String, Long, Long)): User = {
    val user =
      copy(avatar = param._1, nickName = param._2, phone = param._3, email = param._4, updateBy = param._5, updateAt = LocalDateTime.now())
    user.changeRole(Role.justId(param._6))
  }

}

object User {

  implicit val format: OFormat[User] = Json.format[User]

  def entryPwd(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
}
