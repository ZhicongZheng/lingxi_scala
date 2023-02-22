package application.service

import application.command.ActionCommand
import com.google.inject.Inject
import domain.action.ActionRepository
import play.api.mvc.Request

import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ActionService @Inject() (actionRepository: ActionRepository) {

  def onAction(command: ActionCommand)(implicit request: Request[_]): Future[Unit] = actionRepository.save(command).map(_ => ())
}
