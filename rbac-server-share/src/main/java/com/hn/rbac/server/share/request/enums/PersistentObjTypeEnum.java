package com.hn.rbac.server.share.request.enums;

import org.springframework.util.StringUtils;

public enum PersistentObjTypeEnum {
    USER("user"),
    ROLE("role"),
    MENU("menu"),
    FUNCTION("function"),
    USER_ROLE("user_role"),
    ROLE_MENU("role_menu"),
    MENU_FUNCTION("menu_function")
    ;

    private String value;

    PersistentObjTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PersistentObjTypeEnum getByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        for (PersistentObjTypeEnum objTypeEnum : values()) {
            if (objTypeEnum.getValue().equals(value)) {
                return objTypeEnum;
            }
        }
        return null;
    }
}
