package application.command

import common.{Constant, Ip2Region}
import domain.action.Action
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.Request

case class ActionCommand(
  id: Option[Long] = None,
  typ: Int,
  resourceId: Long,
  resourceInfo: String
)

object ActionCommand {

  implicit val format: OFormat[ActionCommand] = Json.format[ActionCommand]

  implicit def toDo(cmd: ActionCommand)(implicit request: Request[_]): Action = Action(
    cmd.id.getOrElse(Constant.domainCreateId),
    cmd.typ,
    cmd.resourceId,
    cmd.resourceInfo,
    Ip2Region.parseIp(request),
    Ip2Region.parseAddress(request)
  )
}
