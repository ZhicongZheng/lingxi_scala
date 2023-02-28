package infra.db.assembler

import domain.action.Action
import infra.db.po.ActionPo

import scala.language.implicitConversions

object ActionAssembler {

  implicit def fromDo(a: Action): ActionPo =
    ActionPo(a.id, a.typ, a.resourceId, a.resourceInfo, a.remoteIp, a.remoteAddress, a.createAt)

  implicit def toDo(po: ActionPo): Action =
    Action(po.id, po.typ, po.resourceId, po.resourceInfo, po.remoteIp, po.remoteAddress, po.createAt)

}
