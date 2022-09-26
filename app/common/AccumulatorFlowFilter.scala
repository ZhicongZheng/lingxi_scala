package common

import akka.NotUsed
import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.scaladsl.Flow
import akka.util.ByteString
import play.api.Logging
import play.api.libs.streams.Accumulator
import play.api.mvc.{EssentialAction, EssentialFilter, RequestHeader, Result}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class AccumulatorFlowFilter @Inject()(actorSystem: ActorSystem)(implicit ec: ExecutionContext) extends EssentialFilter {

  private val logger = org.slf4j.LoggerFactory.getLogger("application.AccumulatorFlowFilter")

  private implicit val logging = Logging(actorSystem.eventStream, logger.getName)


  override def apply(next: EssentialAction): EssentialAction = (requestHeader: RequestHeader) => {

    val accumulator: Accumulator[ByteString, Result] = next(requestHeader)

    val flow: Flow[ByteString, ByteString, NotUsed] = Flow[ByteString].log("byteflow")
    val accumulatorWithResult = accumulator.through(flow).map { result =>
      logger.info(s"The flow has completed and the result is $result")
      result
    }

    accumulatorWithResult

//    val startTime = System.currentTimeMillis
//
//    val accumulator = next(requestHeader)
//
//    accumulator.map { result =>
//      val endTime = System.currentTimeMillis
//      val requestTime = endTime - startTime
//
//      logger.info(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}"
//      )
//      result.withHeaders("Request-Time" -> requestTime.toString)
//    }
  }
}
