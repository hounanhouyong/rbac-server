package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.RoleDO;
import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.share.request.enums.StatusEnum;

public interface RoleCommandService extends IService<RoleDO> {

    Long createRole(Role role, String MenuIds);

    void updateRole(Role role, String menuIds) throws Exception;

    void updateRoleStatus(Long roleId, StatusEnum statusEnum) throws Exception;
}
