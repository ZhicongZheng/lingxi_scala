package infra.db.assembler

import domain.comment.Comment
import infra.db.po.CommentsPo
import io.scalaland.chimney.dsl._

import scala.language.implicitConversions

object CommentAssembler {

  implicit def toPo(c: Comment): CommentsPo = c.into[CommentsPo].transform

}
