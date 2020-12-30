package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.service.entity.RoleDO;
import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.share.request.query.RoleQuery;
import com.hn.rbac.server.share.request.query.SortModel;

import java.util.List;

public interface RoleQueryService extends IService<RoleDO> {

    Long findTotal(RoleQuery roleQuery, List<SortModel> sortModels);

    List<Role> findRoles(RoleQuery roleQuery, List<SortModel> sortModels);

    PageInfo<Role> findRolePage(RoleQuery roleQuery, List<SortModel> sortModels);

    List<Role> findUserRole(String username);

    Role findByName(String roleName);

    Role findValidRoleById(Long id);

    List<Role> findValidRolesByBatchIds(List<Long> idList);

    List<Role> findByBatchIdsAndStatus(List<Long> idList, StatusEnum statusEnum);

    boolean hasValidSuperAdminRole(List<Long> idList);

}
