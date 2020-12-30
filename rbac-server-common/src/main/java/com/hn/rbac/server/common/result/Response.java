package com.hn.rbac.server.common.result;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * 说明：网关层的返回格式
 */
public class Response extends HashMap<String, Object> {

    private static final long serialVersionUID = -1816326123734724270L;

    public Response ok(boolean ok) {
        this.put("ok", ok);
        return this;
    }

    public Response error(HttpStatus httpStatus, String message) {
        this.put("error", new Error().error(httpStatus.value(), httpStatus.getReasonPhrase(), message));
        return this;
    }

    public Response error(HttpStatus httpStatus, String message, String detail) {
        this.put("error", new Error().error(httpStatus.value(), httpStatus.getReasonPhrase(), message, detail));
        return this;
    }

    public Response result(Object data) {
        this.put("result", data);
        return this;
    }

    public Response success() {
        this.ok(true);
        this.result(null);
        return this;
    }

    public Response success(Object data) {
        this.ok(true);
        this.result(data);
        return this;
    }

    public Response fail() {
        this.ok(false);
        return this;
    }

    public Response fail(HttpStatus httpStatus, String message) {
        this.ok(false);
        this.error(httpStatus, message);
        return this;
    }

    public Response fail(HttpStatus httpStatus, String message, String detail) {
        this.ok(false);
        this.error(httpStatus, message, detail);
        return this;
    }

    @Override
    public Response put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
