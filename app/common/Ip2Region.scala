package common

import org.lionsoul.ip2region.xdb.Searcher
import play.api.Play
import play.api.mvc.Request

object Ip2Region {

  private[this] val searcher: Searcher = Searcher.newWithBuffer(Play.getClass.getResourceAsStream("/ipdb/ip2region.xdb").readAllBytes())

  def search(ip: String): String = searcher.search(ip)

  def parseIp(request: Request[_]): String = {
    val remoteIp = request.remoteAddress
    if ("0:0:0:0:0:0:0:1" == remoteIp) {
      return "127.0.0.1"
    }
    val realIp = request.headers.get("X-Real-IP").filter(checkIP)
//    val forwardedFor = request.headers.get("X-Forwarded-For").filter(checkIP)
    realIp.getOrElse(request.remoteAddress)
  }

  def parseAddress(request: Request[_]): String = {
    val ip = parseIp(request)
    search(ip)
  }

  def checkIP(ip: String): Boolean =
    ip.nonEmpty && "unkown".equalsIgnoreCase(ip) && ip.split(".").length == 4

}
