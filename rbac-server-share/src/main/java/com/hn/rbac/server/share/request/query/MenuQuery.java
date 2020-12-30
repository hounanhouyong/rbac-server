package com.hn.rbac.server.share.request.query;

import com.hn.rbac.server.share.request.enums.MenuTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

import java.util.List;

@Data
public class MenuQuery extends BaseQuery {
    private static final long serialVersionUID = 1549844190046975492L;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 菜单/按钮名称
     */
    private String menuName;
    /**
     * 类型
     * @see MenuTypeEnum
     */
    private String type;
    /**
     * 状态
     * @see StatusEnum
     */
    private Integer status;
    /**
     * 菜单名称模糊查询
     */
    private String menuNameKeyWords;
    /**
     * 菜单ID集合
     */
    private List<Long> batchIds;
}
