package com.hn.rbac.server.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hn.rbac.server.share.request.enums.RoleTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
@TableName("role")
public class RoleDO extends BaseDO {
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
