package com.hn.rbac.server.web.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class RoleMenusVO implements Serializable {
    private static final long serialVersionUID = -8156665422547671719L;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色描述
     */
    private String remark;
    /**
     * 菜单集合
     */
    private List<RoleMenuVO> menuList = new ArrayList<>();
}
