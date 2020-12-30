package com.hn.rbac.server.share.result;

import com.hn.rbac.server.share.enums.ErrorCode;

import java.io.Serializable;

public class BaseResult implements Serializable {

    private static final long serialVersionUID = 7004437600754453871L;

    private boolean success;
    private int code;
    private String message;
    
    public BaseResult() {
    }

    public BaseResult(boolean success) {
        this.success = success;
    }
    
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BaseResult error(int code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
        return this;
    }

    public BaseResult error(ErrorCode resultCode) {
        this.success = false;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        return this;
    }
    
    public BaseResult error(ErrorCode resultCode, String msg) {
        this.success = false;
        this.code = resultCode.getCode();

        if (msg != null && msg.length() > 0) {
            this.message = String.format(resultCode.getMessage(), msg);
        } else {
            this.message = resultCode.getMessage();
        }
        return this;
    }
}