package com.hn.rbac.server.web.common.exception;

public class SystemException extends RuntimeException {
    public SystemException() {
        super();
    }
    public SystemException(String message) {
        super(message);
    }
}
