package com.hn.rbac.server.share.model;

import lombok.Data;

@Data
public class RoleMenu extends BaseModel {

    private static final long serialVersionUID = -5200596408874170216L;
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
