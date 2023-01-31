package infra.db.assembler

import domain.action.Action
import infra.db.po.ActionPo

import scala.language.implicitConversions

object ActionAssembler {

  implicit def fromDo(a: Action): ActionPo =
    ActionPo(a.id, a.typ, a.resourceId, a.remoteAddress, a.createBy, a.updateBy, a.createAt, a.updateAt)

  implicit def toDo(po: ActionPo): Action =
    Action(po.id, po.typ, po.resourceId, po.remoteAddress, po.createBy, po.updateBy, po.createAt, po.updateAt)

}
