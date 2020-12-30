package com.hn.rbac.server.common.exception;

/**
 * 限流异常
 */
public class LimitAccessException extends Exception {
    private static final long serialVersionUID = -1510230923949984006L;

    public LimitAccessException(String message) {
        super(message);
    }
}