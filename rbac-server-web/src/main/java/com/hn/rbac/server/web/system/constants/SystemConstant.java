package com.hn.rbac.server.web.system.constants;

public class SystemConstant {

    /**
     * permissions code
     */
    // user
    public static final String FUNCTION_CODE_USER_VIEW = "ADMIN_PLATFORM:USER:VIEW";
    public static final String FUNCTION_CODE_USER_ADD = "ADMIN_PLATFORM:USER:ADD";
    public static final String FUNCTION_CODE_USER_UPDATE = "ADMIN_PLATFORM:USER:UPDATE";
    public static final String FUNCTION_CODE_USER_DISABLE = "ADMIN_PLATFORM:USER:DISABLE";
    public static final String FUNCTION_CODE_USER_ENABLE = "ADMIN_PLATFORM:USER:ENABLE";
    // employee
    public static final String FUNCTION_CODE_USER_EMPLOYEE_VIEW = "ADMIN_PLATFORM:EMPLOYEE:VIEW";
    // role
    public static final String FUNCTION_CODE_ROLE_VIEW = "ADMIN_PLATFORM:ROLE:VIEW";
    public static final String FUNCTION_CODE_ROLE_ADD = "ADMIN_PLATFORM:ROLE:ADD";
    public static final String FUNCTION_CODE_ROLE_UPDATE = "ADMIN_PLATFORM:ROLE:UPDATE";
    public static final String FUNCTION_CODE_ROLE_DISABLE = "ADMIN_PLATFORM:ROLE:DISABLE";
    public static final String FUNCTION_CODE_ROLE_ENABLE = "ADMIN_PLATFORM:ROLE:ENABLE";
    // menu
    public static final String FUNCTION_CODE_MENU_VIEW = "ADMIN_PLATFORM:MENU:VIEW";
    public static final String FUNCTION_CODE_MENU_ADD = "ADMIN_PLATFORM:MENU:ADD";
    public static final String FUNCTION_CODE_MENU_UPDATE = "ADMIN_PLATFORM:MENU:UPDATE";
    public static final String FUNCTION_CODE_MENU_DISABLE = "ADMIN_PLATFORM:MENU:DISABLE";
    public static final String FUNCTION_CODE_MENU_ENABLE = "ADMIN_PLATFORM:MENU:ENABLE";
    public static final String FUNCTION_CODE_MENU_BIND_FUNCTION = "ADMIN_PLATFORM:MENU:BIND_FUNCTION";
    // function
    public static final String FUNCTION_CODE_FUNCTION_VIEW = "ADMIN_PLATFORM:FUNCTION:VIEW";
    public static final String FUNCTION_CODE_FUNCTION_ADD = "ADMIN_PLATFORM:FUNCTION:ADD";
    public static final String FUNCTION_CODE_FUNCTION_UPDATE = "ADMIN_PLATFORM:FUNCTION:UPDATE";
    public static final String FUNCTION_CODE_FUNCTION_DISABLE = "ADMIN_PLATFORM:FUNCTION:DISABLE";
    public static final String FUNCTION_CODE_FUNCTION_ENABLE = "ADMIN_PLATFORM:FUNCTION:ENABLE";
    // opLog
    public static final String FUNCTION_CODE_OPERATION_LOG_VIEW = "ADMIN_PLATFORM:OPERATION_LOG:VIEW";


    /**
     * error message
     */

    public static final String MSG_ADD_USER_FAIL = "新增用户失败";
    public static final String MSG_DISABLED_USER_FAIL = "禁用用户失败";
    public static final String MSG_ENABLED_USER_FAIL = "启用用户失败";
    public static final String MSG_UPDATE_USER_FAIL = "修改角色失败";
    public static final String MSG_DELETE_USER_CACHE_FAIL = "清除用户缓存失败";
    public static final String MSG_USER_ALREADY_DISABLED = "用户(%s)已被禁用";
    public static final String MSG_USER_NOT_EXIST = "用户(%s)不存在";
    public static final String MSG_USER_NOT_ALLOW_MODIFY_OWN_ROLE = "用户(%s)不允许修改自己的角色";

    public static final String MSG_ADD_ROLE_FAIL = "新增角色失败";
    public static final String MSG_DISABLED_ROLE_FAIL = "禁用角色失败";
    public static final String MSG_ENABLED_ROLE_FAIL = "启用角色失败";
    public static final String MSG_UPDATE_ROLE_FAIL = "修改角色失败";

    public static final String MSG_ADD_MENU_FAIL = "新增菜单/按钮失败";
    public static final String MSG_DISABLED_MENU_FAIL = "禁用菜单失败";
    public static final String MSG_ENABLED_MENU_FAIL = "启用菜单失败";
    public static final String MSG_UPDATE_MENU_FAIL = "修改菜单失败";
    public static final String MSG_MENU_BIND_FUNCTION_FAIL = "菜单绑定接口功能失败";
    public static final String MSG_NO_PARENT_MENU_URL_MUST_BE_NULL = "一级菜单URL必须为空";

    public static final String MSG_ADD_FUNCTION_FAIL = "新增接口失败";
    public static final String MSG_DISABLED_FUNCTION_FAIL = "禁用接口失败";
    public static final String MSG_ENABLED_FUNCTION_FAIL = "启用接口失败";
    public static final String MSG_UPDATE_FUNCTION_FAIL = "修改接口失败";

    public static final String MSG_USER_NAME_EXIST = "用户名称(%s)已存在";
    public static final String MSG_ROLE_NAME_EXIST = "角色名称(%s)已存在";
    public static final String MSG_MENU_NAME_EXIST = "菜单名称(%s)已存在";
    public static final String MSG_FUNCTION_NAME_EXIST = "系统(%s)功能名称(%s)已存在";
    public static final String MSG_FUNCTION_CODE_EXIST = "功能权限码(%s)已存在";

}
