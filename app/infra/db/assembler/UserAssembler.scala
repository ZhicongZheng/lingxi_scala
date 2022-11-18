package infra.db.assembler

import domain.user.entity.User
import infra.db.po.UserPo

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.concurrent.ExecutionContext.Implicits.global

object UserAssembler {

  implicit def toDo(p: UserPo): User =
    User(p.id, p.username, p.password, p.avatar, p.nickName, p.phone, p.email, None, p.createBy, p.updateBy, p.createAt, p.updateAt)

  implicit def toDoSeq(seq: Seq[UserPo]): Seq[User] = seq.map(toDo)

  implicit def toDoOpt(opt: Option[UserPo]): Option[User] = opt.map(toDo)

  implicit def fromDo(t: User): UserPo =
    UserPo(t.id, t.username, t.password, t.avatar, t.nickName, t.phone, t.email, t.createBy, t.updateBy, t.createAt, t.updateAt)

}
