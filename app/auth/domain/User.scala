package auth.domain

import common.ResponseCode
import common.exceptions.BizException
import org.mindrot.jbcrypt.BCrypt
import utils.Jwt

import java.time.LocalDateTime
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

final case class User(id: Long,
                      username: String,
                      password: String,
                      avatar: String,
                      nickName: String,
                      roles: Option[Seq[Role]] = None,
                      createBy: Long = 0L,
                      updateBy: Long = 0L,
                      createAt: LocalDateTime = LocalDateTime.now(),
                      updateAt: Option[LocalDateTime] = None) extends BaseInfo {

  def login(reqPassword: String): Future[String] = {
    Try(BCrypt.checkpw(reqPassword, password)) match {
      case Success(res) if res => Future.successful(Jwt.createToken(id.toString))
      case Success(res) =>     Future.failed(new BizException(ResponseCode.LOGIN_FAILED))
      case Failure(exception) =>     Future.failed(new BizException(ResponseCode.LOGIN_FAILED))
    }
  }

}




