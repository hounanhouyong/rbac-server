package com.hn.rbac.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.PersistentLogCommandService;
import com.hn.rbac.server.service.UserRoleCommandService;
import com.hn.rbac.server.service.dao.UserRoleMapper;
import com.hn.rbac.server.service.entity.UserRoleDO;
import com.hn.rbac.server.share.model.PersistentLog;
import com.hn.rbac.server.share.request.enums.PersistentObjTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserRoleCommandServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements UserRoleCommandService {

    @Resource
    private PersistentLogCommandService persistentLogCommandService;

    @Override
    public void saveUserRoles(Long userId, String[] roleIdArray) {
        if (userId == null) {
            return;
        }
        if (roleIdArray == null || roleIdArray.length == 0) {
            return;
        }
        Date current = new Date();
        Arrays.stream(roleIdArray).forEach(roleId -> {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setGmtCreate(current);
            userRoleDO.setGmtModified(current);
            userRoleDO.setUserId(userId);
            userRoleDO.setRoleId(Long.valueOf(roleId));
            baseMapper.insert(userRoleDO);

            // 变更记录持久化
            persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                            .objType(PersistentObjTypeEnum.USER_ROLE.getValue())
                                                                            .objId(userRoleDO.getId())
                                                                            .modifiedValue(JSON.toJSONString(userRoleDO))
                                                                            .build());
        });
    }

    @Transactional
    @Override
    public void deleteUserRolesByRoleId(Long... roleIds) {
        if (roleIds == null) {
            return;
        }
        Arrays.stream(roleIds).forEach(id -> {
            baseMapper.delete(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getRoleId, id));
        });
    }

    @Transactional
    @Override
    public void deleteUserRolesByUserId(Long... userIds) {
        if (userIds == null) {
            return;
        }
        Arrays.stream(userIds).forEach(id -> {
            baseMapper.delete(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getUserId, id));
        });
    }

}
