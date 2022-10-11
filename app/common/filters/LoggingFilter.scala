package common.filters

import akka.stream.Materializer
import play.api.Logging
import play.api.mvc.{Filter, RequestHeader, Result}
import play.api.routing.{HandlerDef, Router}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

/**
 * 请求耗时过滤器
 */
class LoggingFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter with Logging {

  override def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis()

    nextFilter(requestHeader).map { result =>
//      val handlerDef: HandlerDef = requestHeader.attrs(Router.Attrs.HandlerDef)
//      val action = handlerDef.controller + "." + handlerDef.method
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime

      logger.info(s"took ${requestTime}ms and returned ${result.header.status}")
      result.withHeaders("Request-Time" -> requestTime.toString)

    }

  }
}
