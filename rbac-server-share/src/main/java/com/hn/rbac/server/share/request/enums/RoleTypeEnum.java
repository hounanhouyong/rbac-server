package com.hn.rbac.server.share.request.enums;

public enum RoleTypeEnum {
    ADMIN("admin"),
    SUPER_ADMIN("superadmin");

    private String value;

    RoleTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
