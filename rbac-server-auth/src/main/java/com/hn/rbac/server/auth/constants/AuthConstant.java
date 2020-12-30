package com.hn.rbac.server.auth.constants;

public class AuthConstant {

    public static final String CONTEXT_KEY_USERNAME = "username";
    public static final String CONTEXT_KEY_SECURITY_TOKEN = "securityToken";
    public static final String CONTEXT_KEY_SYSTEM_USER = "systemUser";
    public static final String CONTEXT_KEY_REQUEST_MODULE = "requestModule";

    public static final int SYSTEM_USER = 1;
    public static final int NO_SYSTEM_USER = 0;

    public static final String MSG_TOKEN_AUTH_NO_PASS = "token校验不通过";
    public static final String MSG_NO_AUTH = "未认证，请先进行认证";
    public static final String MSG_NO_SYSTEM_USER = "请管理员添加账号(%s)，账号管理->新增人员";
}
