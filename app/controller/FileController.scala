package controller

import common.Results
import common.actions.UserAction
import common.oss.OssRepository
import common.result.FILE_EMPTY
import play.api.mvc.{ControllerComponents, InjectedController}

import java.io.FileInputStream
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class FileController @Inject() (
  override val controllerComponents: ControllerComponents,
  authedAction: UserAction,
  ossRepository: OssRepository
) extends InjectedController {

  def upload = authedAction(parse.multipartFormData) async { request =>
    request.body
      .file("file")
      .fold(Future.successful(Results.fail(FILE_EMPTY))) { file =>
        val input = new FileInputStream(file.ref)
        ossRepository.upload(input, file.filename).map(path => Results.success(path)).recover(ex => Results.fail(ex))
      }
  }

}
