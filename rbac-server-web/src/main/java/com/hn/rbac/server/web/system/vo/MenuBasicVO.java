package com.hn.rbac.server.web.system.vo;

import com.hn.rbac.server.share.request.enums.MenuTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class MenuBasicVO implements Serializable {
    private static final long serialVersionUID = 6859897700348022495L;
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
     * 上级菜单集合
     */
    private String parentIds;
}
