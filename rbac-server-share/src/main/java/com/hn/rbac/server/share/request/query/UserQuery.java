package com.hn.rbac.server.share.request.query;

import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
public class UserQuery extends BaseQuery {
    private static final long serialVersionUID = -1622403254094314474L;
    /**
     * 用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String displayName;
    /**
     * 状态
     * @see StatusEnum
     */
    private Integer status;
    /**
     * username模糊查询
     */
    private String usernameKeyWords;
    /**
     * roleName模糊查询
     */
    private String roleNameKeyWords;
    /**
     * displayName模糊查询
     */
    private String displayNameKeyWords;
}
