package com.hn.rbac.server.web.system.request;

import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.request.enums.MenuTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.web.system.request.converter.MenuRequestConverter;
import lombok.Data;

import java.io.Serializable;

@Data
public class MenuRequest implements Serializable {
    private static final long serialVersionUID = 3585282499371104329L;
    /**
     * 菜单ID
     */
    private Long id;
    /**
     * 上级菜单ID
     */
    private Long parentId;
    /**
     * 菜单/按钮名称
     */
    private String menuName;
    /**
     * 菜单URL
     */
    private String url;
    /**
     * 目标URL
     */
    private String targetUrl;
    /**
     * 图标
     */
    private String icon;
    /**
     * 权限标识
     */
    private String perms;
    /**
     * 类型
     * @see MenuTypeEnum
     */
    private String type;
    /**
     * 排序
     */
    private Long orderNum;
    /**
     * 状态
     * @see StatusEnum
     */
    private Integer status;
    /**
     * 菜单说明
     */
    private String remark;
    /**
     * 上级菜单集合
     */
    private String parentIds;
    /**
     * 菜单排序ID集合
     * 1,2,3
     */
    private String sortedIds;
    /**
     * 功能接口ID集合
     * 1,2,3
     */
    private String functionIds;

    public Menu buildMenu() {
        return MenuRequestConverter.INSTANCE.to(this);
    }

}
