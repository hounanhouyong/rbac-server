package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.UserDO;
import com.hn.rbac.server.share.model.User;
import com.hn.rbac.server.share.request.enums.StatusEnum;

public interface UserCommandService extends IService<UserDO> {

    Long createUser(User user, String roleIds) throws Exception;

    /**
     * 只是更新用户角色集合，暂时不支持更新用户信息项
     * 说明：roleIds=''时，表示清除用户所有角色
     * @param userId
     * @param roleIds
     * @throws Exception
     */
    void updateUser(Long userId, String roleIds) throws Exception;

    void updateUserStatus(Long userId, StatusEnum statusEnum) throws Exception;
}
