package com.hn.rbac.server.share.request.enums;

import org.springframework.util.StringUtils;

public enum MenuTypeEnum {
    APP("app"),
    MENU("menu"),
    PAGE("page"),
    FUNCTION("function");

    private String value;

    MenuTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MenuTypeEnum getByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        for (MenuTypeEnum menuTypeEnum : values()) {
            if (menuTypeEnum.getValue().equals(value)) {
                return menuTypeEnum;
            }
        }
        return null;
    }
}
