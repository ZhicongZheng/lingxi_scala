package auth.domain

import java.time.LocalDateTime
import scala.concurrent.Future

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
    Future.successful("")
  }

}




