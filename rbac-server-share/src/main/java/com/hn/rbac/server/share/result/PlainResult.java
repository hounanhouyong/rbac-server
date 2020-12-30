package com.hn.rbac.server.share.result;

import com.hn.rbac.server.share.enums.ErrorCode;

import java.io.Serializable;

public class PlainResult<T> extends BaseResult implements Serializable {

    private static final long serialVersionUID = -75579514934971078L;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> PlainResult<T> success(T data) {
        PlainResult<T> result = new PlainResult<>();
        result.setSuccess(true);
        result.setCode(ErrorCode.SUCCESS.getCode());
        result.setMessage(ErrorCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> PlainResult<T> failed(T data,int code,String message) {
        PlainResult<T> result = new PlainResult<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}