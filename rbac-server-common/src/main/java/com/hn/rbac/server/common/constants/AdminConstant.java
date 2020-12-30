package com.hn.rbac.server.common.constants;

public class AdminConstant {

    public static final String APP_NAME = "operate-admin";

    // user缓存前缀
    public static final String USER_CACHE_PREFIX = "operate.admin.cache.user.";
    // user角色缓存前缀
    public static final String USER_ROLE_CACHE_PREFIX = "operate.admin.cache.user.role.";
    // user权限缓存前缀
    public static final String USER_PERMISSION_CACHE_PREFIX = "operate.admin.cache.user.permission.";
    // token缓存前缀
    public static final String TOKEN_CACHE_PREFIX = "operate.admin.cache.token.";

    // 存储在线用户的 zset前缀
    public static final String ACTIVE_USERS_ZSET_PREFIX = "operate.admin.user.active";

    public static final String USER_SECURITY_TOKEN_HSET_KEY_NAME = "operate.admin.user.security.tokens";

    // 异步线程池名称
    public static final String ASYNC_POOL = "operateAdminAsyncThreadPool";

    // 跟菜单ID
    public static final Long MENU_ROOT_ID = 0L;
    // 默认菜单排序编号
    public static final Long DEFAULT_ORDER_NUM = 100L;
    // 默认密码
    public static final String DEFAULT_PASSWORD = "1234abcd";
    // 默认权限码前缀
    public static final String DEFAULT_PERMS_PREFIX = "PERMS_%id%";
    // 默认系统名称
    public static final String DEFAULT_APP_NAME = "全局通用";


}
