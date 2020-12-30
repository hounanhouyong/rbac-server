package com.hn.rbac.server.share.model;

import com.hn.rbac.server.share.request.enums.MenuTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
public class Menu extends BaseModel {
    private static final long serialVersionUID = -1413062436771875041L;
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
}
