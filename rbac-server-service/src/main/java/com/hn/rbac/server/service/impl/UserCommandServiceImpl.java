package com.hn.rbac.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.PersistentLogCommandService;
import com.hn.rbac.server.service.UserCommandService;
import com.hn.rbac.server.service.UserQueryService;
import com.hn.rbac.server.service.UserRoleCommandService;
import com.hn.rbac.server.service.dao.UserMapper;
import com.hn.rbac.server.service.entity.UserDO;
import com.hn.rbac.server.service.entity.converter.UserConverter;
import com.hn.rbac.server.service.manager.UserManager;
import com.hn.rbac.server.share.model.PersistentLog;
import com.hn.rbac.server.share.model.User;
import com.hn.rbac.server.share.request.enums.PersistentObjTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserCommandServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserCommandService {

    @Resource
    private UserRoleCommandService userRoleCommandService;
    @Resource
    private UserManager userManager;
    @Resource
    private PersistentLogCommandService persistentLogCommandService;
    @Resource
    private UserQueryService userQueryService;

    @Override
    @Transactional
    public Long createUser(User user, String roleIds) throws Exception {
        // 创建用户
        user.setGmtCreate(new Date());
        user.setStatus(StatusEnum.VALID.getValue());
        UserDO userDO = UserConverter.INSTANCE.to(user);
        save(userDO);

        // 保存用户角色
        if (StringUtils.isNotEmpty(roleIds)) {
            String[] roleIdArray = roleIds.split(StringPool.COMMA);
            userRoleCommandService.saveUserRoles(userDO.getId(), roleIdArray);
        }

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.USER.getValue())
                                                                        .objId(userDO.getId())
                                                                        .modifiedValue(JSON.toJSONString(userDO))
                                                                        .build());

        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user.getUsername());

        return userDO.getId();
    }

    @Override
    @Transactional
    public void updateUser(Long userId, String roleIds) throws Exception {
        Assert.isTrue(userId != null && userId > 0L, "Null userId");
        // user
        User user = userQueryService.findById(userId);
        if (user == null) {
            return;
        }

        // 暂时不支持更新用户信息

        /**
         * 用户角色更新说明：
         * 情况1：roleIds=null，不做处理
         * 情况2：roleIds=''，即roleIds.length() == 0，清除旧的
         * 情况3：roleIds.length() > 0, 清除旧的，保存新的
         */
        if (roleIds == null) {
            return;
        }

        // 清理旧的角色
        userRoleCommandService.deleteUserRolesByUserId(userId);

        // 更新用户角色
        if (StringUtils.isNotEmpty(roleIds)) {
            // 保存新的角色
            String[] roleArray = roleIds.split(StringPool.COMMA);
            userRoleCommandService.saveUserRoles(userId, roleArray);
        }

        // 更新缓存，加载用户信息到Redis
        userManager.loadUserRedisCache(user.getUsername());
    }

    @Override
    public void updateUserStatus(Long userId, StatusEnum statusEnum) throws Exception {
        Assert.isTrue(userId != null, "Null userId");

        User user = userQueryService.findById(userId);
        if (user == null) {
            return;
        }

        UserDO userDO = new UserDO();
        userDO.setId(userId);
        userDO.setStatus(statusEnum.getValue());
        userDO.setGmtModified(new Date());
        baseMapper.updateById(userDO);

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.USER.getValue())
                                                                        .objId(userDO.getId())
                                                                        .modifiedValue(JSON.toJSONString(userDO))
                                                                        .build());

        // 更新缓存，加载用户信息到Redis
        userManager.loadUserRedisCache(user.getUsername());
    }

}
