package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.UserRoleDO;

public interface UserRoleCommandService extends IService<UserRoleDO> {

    void saveUserRoles(Long userId, String[] roleIdArray);

    void deleteUserRolesByRoleId(Long... roleIds);

    void deleteUserRolesByUserId(Long... userIds);
}
