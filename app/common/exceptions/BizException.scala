package common.exceptions

import common.result.Errors

case class BizException(code: Int, message: String, err: Option[Errors] = None) extends RuntimeException(message) {}

object BizException {

  def apply(err: Errors): BizException = BizException(err.code, err.message, Some(err))

  def apply(errCode: Int, errMsg: String): BizException = {

    val err = Some(
      new Errors {
        override val code: Int       = errCode
        override val message: String = errMsg
      })
    BizException(errCode, errMsg, err)
  }
}
