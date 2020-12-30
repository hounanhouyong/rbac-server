package com.hn.rbac.server.share.model;

import lombok.Data;

@Data
public class UserRole extends BaseModel {

    private static final long serialVersionUID = 2354394771912648574L;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;

}
