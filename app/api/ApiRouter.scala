package api

import akka.stream.Materializer
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import sttp.tapir.server.play.{PlayServerInterpreter, PlayServerOptions}
import sttp.tapir.swagger.SwaggerUI

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApiRouter @Inject() (apiDocumentation: ApiDocumentation)(implicit
  val materialize: Materializer,
  ec: ExecutionContext
) extends SimpleRouter {

  private val playServerOptions: PlayServerOptions = PlayServerOptions.default(materialize, ec)
  private val interpreter                          = PlayServerInterpreter(playServerOptions)

  override def routes: Routes =
    openApiRoute

  // Doc will be on /docs
  private val openApiRoute: Routes = interpreter.toRoutes(SwaggerUI[Future](apiDocumentation.openApiYaml))
}
