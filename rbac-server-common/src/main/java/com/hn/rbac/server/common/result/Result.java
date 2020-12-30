package com.hn.rbac.server.common.result;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class Result extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    public Result code(HttpStatus status) {
        this.put("code", status.value());
        return this;
    }

    public Result message(String message) {
        this.put("message", message);
        return this;
    }

    public Result data(Object data) {
        this.put("data", data);
        return this;
    }

    public Result success() {
        this.code(HttpStatus.OK);
        this.data(null);
        this.message("");
        return this;
    }

    public Result success(Object data) {
        this.code(HttpStatus.OK);
        this.data(data);
        this.message("");
        return this;
    }

    public Result fail() {
        this.code(HttpStatus.INTERNAL_SERVER_ERROR);
        return this;
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
