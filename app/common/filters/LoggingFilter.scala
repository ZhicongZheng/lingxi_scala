package common.filters

import akka.stream.Materializer
import play.api.Logging
import play.api.mvc.{Filter, RequestHeader, Result}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

/** 请求耗时过滤器
 */
class LoggingFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext) extends Filter with Logging {

  override def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis()

    nextFilter(requestHeader).map { result =>
      val path        = requestHeader.path
      val method      = requestHeader.method
      val endTime     = System.currentTimeMillis
      val requestTime = endTime - startTime

      logger.info(s" $method  $path, status: ${result.header.status}, took ${requestTime}ms")
      result.withHeaders("Request-Time" -> requestTime.toString)
    }

  }
}
