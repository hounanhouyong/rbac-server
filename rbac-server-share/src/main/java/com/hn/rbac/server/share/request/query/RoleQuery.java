package com.hn.rbac.server.share.request.query;

import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
public class RoleQuery extends BaseQuery {
    private static final long serialVersionUID = 1130427861316652689L;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 状态
     * @see StatusEnum
     */
    private Integer status;
    /**
     * 角色名称模糊查询
     */
    private String roleNameKeyWords;
}
