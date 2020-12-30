package com.hn.rbac.server.web.system.request;

import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.web.system.request.converter.RoleRequestConverter;
import lombok.Data;

import java.io.Serializable;


@Data
public class RoleRequest implements Serializable {
    private static final long serialVersionUID = -6801237761028808936L;
    /**
     * 角色ID
     */
    private Long id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色描述
     */
    private String remark;
    /**
     * menuId集合
     * 格式：1:0,2:0,3:1
     */
    private String menuIds;

    public Role buildRole() {
        return RoleRequestConverter.INSTANCE.to(this);
    }
}
