package common.filters

import akka.stream.Materializer
import common.Constant
import common.filters.JwtFilter.{ bearerLen, failureResult, noAuthRoute }
import play.api.Logging
import play.api.http.HeaderNames
import play.api.mvc.Results.Unauthorized
import play.api.mvc._

import java.util.regex.Pattern
import javax.inject.Inject
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Success, Try }

/** jwt 过滤器
 */
class JwtFilter @Inject() (sessionCookieBaker: DefaultSessionCookieBaker)(implicit
  val mat: Materializer,
  ec: ExecutionContext
) extends Filter
    with Logging {

  val jwt: JWTCookieDataCodec = sessionCookieBaker.jwtCodec

  override def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    val path = rh.path
    if (noAuthRoute.exists(p => p.matcher(path).find())) {
      return f.apply(rh)
    }

    val headers = rh.headers
    headers
      .get(HeaderNames.AUTHORIZATION)
      .map(token => token.substring(bearerLen))
      .fold(failureResult) { jwtToken =>
        Try(jwt.decode(jwtToken)) match {
          case Success(claim) if claim.nonEmpty =>
            val requestHeader = claim
              .get(Constant.userId)
              .map(userId => rh.withHeaders(headers.add((Constant.userId, userId))))
              .getOrElse(rh)
            f.apply(requestHeader)
          case _ => failureResult
        }
      }
  }
}

object JwtFilter {
  val noAuthRoute: Seq[Pattern] = Seq(
    Pattern.compile("/"),
    Pattern.compile("/admin/login"),
    Pattern.compile("/docs/*"),
    Pattern.compile("/*.ico")
  )
  val bearerLen: Int                = "Bearer ".length
  val failureResult: Future[Result] = Future.successful(Unauthorized("Invalid credential"))
}
