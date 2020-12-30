package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.UserRoleDO;
import com.hn.rbac.server.share.model.UserRole;

import java.util.List;

public interface UserRoleQueryService extends IService<UserRoleDO> {

    List<Long> findUserIdsByRoleId(Long roleId);

    List<Long> findUserIdsByBatchRoleIds(List<Long> roleIds);

    List<Long> findRoleIdsByUserId(Long userId);

    List<Long> findRoleIdsByBatchUserIds(List<Long> userIds);

    List<UserRole> findByRoleId(Long roleId);

    List<UserRole> findByBatchRoleIds(List<Long> roleIdList);

    List<UserRole> findByUserId(Long userId);

    List<UserRole> findByBatchUserId(List<Long> userIdList);
}
