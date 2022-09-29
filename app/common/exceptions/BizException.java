package common.exceptions;

import common.ResponseCode;

public class BizException extends RuntimeException {

    protected int code;
    protected String message;

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    protected ResponseCode responseCode;

    public BizException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(String message) {
        this.code = ResponseCode.SYSTEM_ERROR.getCode();
        this.message = message;
    }

    public BizException(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.responseCode = responseCode;
    }
}
