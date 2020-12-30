package com.hn.rbac.server.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role_menu")
public class RoleMenuDO extends BaseDO {
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 菜单/按钮ID
     */
    private Long menuId;
    /**
     * 菜单ID下所有菜单是否全选
     */
    private Integer allSelected;
}
