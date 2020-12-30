package com.hn.rbac.server.share.enums;

public enum ErrorCode {

    SUCCESS(200, "successful"),

    // 基础错误码
    BIZ_ERROR(-100, "biz error, msg[s%]"),
    PARAMS_ERROR(-101, "param error, msg[%s]"),
    NO_LOGIN(-102, "no login"),
    NO_PERMISSION(-103, "no permission, msg[s%]"),
    ILLEGAL_OPERATION(-104, "illegal_operation, msg[%]"),

    // 处方专有--业务错误码
    PRESCRIPTION_NOT_EXISTS(-110, "prescription not exists, msg[s%]"),
    PRESCRIPTION_USED(-111, "prescription_used, msg[s%]"),    
    PRESCRIPTION_CANCEL(-112, "prescription_cancel, msg[s%]"),
    PRESCRIPTION_EXPIRED(-113, "prescription_expired, msg[s%]"),

    //处方审核状态
    PRESCRIPTION_NOT_UNAUDIT(-120, "prescription_not_UNAUDIT, msg[s%]"),
    PRESCRIPTION_AUDITED(-121,"prescription_AUDITED,msg[s%]"),
    PRESCRIPTION_REJECTED(-122,"prescription_REJECTED,msg[s%]"),

    // 系统异常 错误码
    SYSTEM_ERROR(-500, "system error, msg[%s]")
    ;

    private int code;
    private String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}