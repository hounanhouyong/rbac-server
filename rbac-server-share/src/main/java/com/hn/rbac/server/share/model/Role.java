package com.hn.rbac.server.share.model;

import com.hn.rbac.server.share.request.enums.RoleTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
public class Role extends BaseModel {

    private static final long serialVersionUID = -4493960686192269860L;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色描述
     */
    private String remark;
    /**
     * 状态
     * @see StatusEnum
     */
    private Integer status;
    /**
     * @see RoleTypeEnum
     */
    private String type;

}
