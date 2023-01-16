package infra.mail

import play.api.libs.mailer.{Email, MailerClient}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait MailService {
  def send(email: Email): Future[Unit]

}

@Singleton
class MailServiceImpl @Inject() (mailerClient: MailerClient) extends MailService {
  override def send(email: Email): Future[Unit] = Future(mailerClient.send(email)).map(_ => ())
}
