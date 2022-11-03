package common.filters

import akka.stream.Materializer
import akka.Done
import common.Constant
import common.filters.AuthenticationFilter.{bearerLen, handleNullToken, handleTokenExpire, handleTokenValidateError, noAuthRoute}
import common.result.TOKEN_CHECK_ERROR
import play.api.Logging
import play.api.cache.AsyncCacheApi
import play.api.http.HeaderNames
import play.api.mvc._

import java.util.regex.Pattern
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.{Success, Try}

/** 认证过滤器，使用JWT bearer token
 */
class AuthenticationFilter @Inject() (cache: AsyncCacheApi, sessionCookieBaker: DefaultSessionCookieBaker)(implicit
  val mat: Materializer,
  ec: ExecutionContext
) extends Filter
    with Logging {

  private[this] val jwt: JWTCookieDataCodec = sessionCookieBaker.jwtCodec
  private[this] val expire: FiniteDuration  = jwt.jwtConfiguration.expiresAfter.getOrElse(30.minutes)

  override def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    val path = rh.path
    if ("/" == path || noAuthRoute.exists(p => p.matcher(path).find())) {
      return f.apply(rh)
    }

    val headers = rh.headers
    headers
      .get(HeaderNames.AUTHORIZATION)
      .filter(token => token.length >= bearerLen)
      .map(token => token.substring(bearerLen))
      .fold(handleNullToken) { jwtToken =>
        // 已经退出登陆，需要重新获取token
        cache.get[String](jwtToken).flatMap {
          case Some(_) => handleTokenExpire
          case None =>
            Try(jwt.decode(jwtToken)) match {
              case Success(claim) if claim.nonEmpty =>
                val requestHeader = claim
                  .get(Constant.userId)
                  .map(userId => rh.withHeaders(headers.add((Constant.userId, userId))))
                  .getOrElse(rh)
                f.apply(requestHeader)
              case _ => handleTokenValidateError
            }
        }
      }
  }

  def logout(jwtToken: String): Future[Done] =
    cache.set(jwtToken.substring(bearerLen), jwtToken, expire)
}

object AuthenticationFilter extends Logging {
  val noAuthRoute: Seq[Pattern] = Seq(
    Pattern.compile("/users/login"),
    Pattern.compile("/docs/*"),
    Pattern.compile("/*.ico")
  )
  val bearerLen: Int = "Bearer ".length

  private def handleNullToken = {
    logger.info("jwt token not found")
    failureResult
  }

  private def handleTokenExpire = {
    logger.info("jwt token is expire")
    failureResult
  }

  private def handleTokenValidateError = {
    logger.info("jwt token validated error")
    failureResult
  }

  private val failureResult: Future[Result] = Future.successful(common.Results.fail(TOKEN_CHECK_ERROR))

}
