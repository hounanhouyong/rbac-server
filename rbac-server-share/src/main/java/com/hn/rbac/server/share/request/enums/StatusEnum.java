package com.hn.rbac.server.share.request.enums;

public enum StatusEnum {
    VALID(0),
    INVALID(1);

    private Integer value;

    StatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
