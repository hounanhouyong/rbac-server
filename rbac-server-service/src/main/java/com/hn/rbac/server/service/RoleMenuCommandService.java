package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.RoleMenuDO;

public interface RoleMenuCommandService extends IService<RoleMenuDO> {

    void saveRoleMenus(Long roleId, String[] menuIds);

    void deleteRoleMenusByRoleId(Long... roleIds);

    void deleteRoleMenusByMenuId(Long... menuIds);
}
