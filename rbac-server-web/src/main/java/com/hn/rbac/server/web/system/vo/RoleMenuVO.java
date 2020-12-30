package com.hn.rbac.server.web.system.vo;

import com.hn.rbac.server.share.model.RoleMenu;
import com.hn.rbac.server.share.request.enums.MenuTypeEnum;
import lombok.Data;


@Data
public class RoleMenuVO extends RoleMenu {
    private static final long serialVersionUID = -5492418483418236924L;
    /**
     * 菜单/按钮名称
     */
    private String menuName;
    /**
     * 菜单类型
     * @see MenuTypeEnum
     */
    private String type;
    /**
     * 菜单URL
     */
    private String url;
    /**
     * 父菜单ID集合
     * 1,2,3,4
     */
    private String parentIds;

}
