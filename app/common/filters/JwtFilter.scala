package common.filters

import akka.stream.Materializer
import common.Constant
import common.filters.JwtFilter.noAuthRoute
import play.api.Logging
import play.api.http.HeaderNames
import play.api.mvc.Results.Unauthorized
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}

/**
 * jwt 过滤器
 */
class JwtFilter @Inject()(sessionCookieBaker: DefaultSessionCookieBaker)
                         (implicit val mat: Materializer, ec: ExecutionContext) extends Filter with Logging {

  val jwt: JWTCookieDataCodec = sessionCookieBaker.jwtCodec

  override def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    if (noAuthRoute.contains(rh.path)) {
      return f.apply(rh)
    }

    val jwtToken = rh.headers.get(HeaderNames.AUTHORIZATION).getOrElse("")

    Try(jwt.decode(jwtToken)) match {
      case Success(claim) if claim.nonEmpty =>
        claim.get(Constant.userId).foreach(userId => rh.headers.add((Constant.userId, userId)))
        f.apply(rh)
      case _ => Future.successful(Unauthorized("Invalid credential"))
    }
  }
}

object JwtFilter {
  val noAuthRoute: Seq[String] = Seq("/admin/login", "/users")
}
