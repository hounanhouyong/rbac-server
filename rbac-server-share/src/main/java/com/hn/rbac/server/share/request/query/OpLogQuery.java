package com.hn.rbac.server.share.request.query;

import lombok.Data;

@Data
public class OpLogQuery extends BaseQuery {
    private static final long serialVersionUID = 3473681436199664344L;
    /**
     * 操作用户
     */
    private String username;
    /**
     * 模块ID
     */
    private Long moduleId;
    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 操作用户模糊搜索
     */
    private String usernameKeyWords;
}
