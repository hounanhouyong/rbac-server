package com.hn.rbac.server.web.system.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRequest implements Serializable {
    private static final long serialVersionUID = 5067507097667437340L;
    /**
     * id
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 角色列表
     * 格式：1,2,3,4
     */
    private String roleIds;

}
