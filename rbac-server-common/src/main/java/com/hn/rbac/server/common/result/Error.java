package com.hn.rbac.server.common.result;

import java.util.HashMap;

public class Error extends HashMap<String, Object> {
    private static final long serialVersionUID = -5965688679531742969L;

    public Error code(int code) {
        this.put("code", code);
        return this;
    }

    public Error reasonPhrase(String reasonPhrase) {
        this.put("reasonPhrase", reasonPhrase);
        return this;
    }

    public Error message(String message) {
        this.put("message", message);
        return this;
    }

    public Error detail(String detail) {
        this.put("detail", detail);
        return this;
    }

    public Error error(int code, String reasonPhrase, String message) {
        this.code(code);
        this.reasonPhrase(reasonPhrase);
        this.message(message);
        return this;
    }

    public Error error(int code, String reasonPhrase, String message, String detail) {
        this.code(code);
        this.reasonPhrase(reasonPhrase);
        this.message(message);
        this.detail(detail);
        return this;
    }

}
