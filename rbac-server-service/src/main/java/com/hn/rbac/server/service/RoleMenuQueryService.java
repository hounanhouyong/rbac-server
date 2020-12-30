package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.RoleMenuDO;
import com.hn.rbac.server.share.model.RoleMenu;

import java.util.List;

public interface RoleMenuQueryService extends IService<RoleMenuDO> {

    List<Long> findMenuIdsByRoleId(Long roleId);

    List<Long> findMenuIdsByBatchRoleIds(List<Long> roleIdList);

    List<Long> findRoleIdsByMenuId(Long menuId);

    List<Long> findRoleIdsByBatchMenuIds(List<Long> menuIdList);

    List<RoleMenu> findByRoleId(Long roleId);

    List<RoleMenu> findByBatchRoleIds(List<Long> roleIdList);
}
