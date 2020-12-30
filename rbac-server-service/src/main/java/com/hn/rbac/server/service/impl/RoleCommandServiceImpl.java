package com.hn.rbac.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.PersistentLogCommandService;
import com.hn.rbac.server.service.RoleCommandService;
import com.hn.rbac.server.service.RoleMenuCommandService;
import com.hn.rbac.server.service.UserRoleQueryService;
import com.hn.rbac.server.service.dao.RoleMapper;
import com.hn.rbac.server.service.entity.RoleDO;
import com.hn.rbac.server.service.entity.converter.RoleConverter;
import com.hn.rbac.server.service.manager.UserManager;
import com.hn.rbac.server.share.model.PersistentLog;
import com.hn.rbac.server.share.model.Role;
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
import java.util.List;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleCommandServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements RoleCommandService {

    @Resource
    private UserRoleQueryService userRoleQueryService;
    @Resource
    private RoleMenuCommandService roleMenuCommandService;
    @Resource
    private UserManager userManager;
    @Resource
    private PersistentLogCommandService persistentLogCommandService;

    @Transactional
    @Override
    public Long createRole(Role role, String menuIds) {
        Date current = new Date();
        role.setGmtCreate(current);
        role.setGmtModified(current);
        role.setStatus(StatusEnum.VALID.getValue());
        RoleDO roleDO = RoleConverter.INSTANCE.to(role);
        baseMapper.insert(roleDO);

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.ROLE.getValue())
                                                                        .objId(roleDO.getId())
                                                                        .modifiedValue(JSON.toJSONString(roleDO))
                                                                        .build());

        if (StringUtils.isNotEmpty(menuIds)) {
            String[] menuIdArray = menuIds.split(StringPool.COMMA);
            roleMenuCommandService.saveRoleMenus(roleDO.getId(), menuIdArray);
        }

        return roleDO.getId();
    }

    @Transactional
    @Override
    public void updateRole(Role role, String menuIds) throws Exception {
        Assert.isTrue(role != null && role.getId() != null, "Null roleId");

        // role -> user
        List<Long> userIds = userRoleQueryService.findUserIdsByRoleId(role.getId());

        // update role
        role.setGmtModified(new Date());
        baseMapper.updateById(RoleConverter.INSTANCE.to(role));

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.ROLE.getValue())
                                                                        .objId(role.getId())
                                                                        .modifiedValue(JSON.toJSONString(role))
                                                                        .build());

        // delete role <-> menu
        roleMenuCommandService.deleteRoleMenusByRoleId(role.getId());
        if (StringUtils.isNotEmpty(menuIds)) {
            // insert role <-> menu
            String[] menuIdArray = menuIds.split(StringPool.COMMA);
            roleMenuCommandService.saveRoleMenus(role.getId(), menuIdArray);
        }

        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserRedisCache(userIds);
    }

    @Override
    public void updateRoleStatus(Long roleId, StatusEnum statusEnum) throws Exception {
        Assert.isTrue(roleId != null, "Null roleId");
        RoleDO roleDO = new RoleDO();
        roleDO.setId(roleId);
        roleDO.setStatus(statusEnum.getValue());
        roleDO.setGmtModified(new Date());
        baseMapper.updateById(roleDO);

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.ROLE.getValue())
                                                                        .objId(roleDO.getId())
                                                                        .modifiedValue(JSON.toJSONString(roleDO))
                                                                        .build());

        // role <-> user
        List<Long> userIds = userRoleQueryService.findUserIdsByRoleId(roleId);

        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserRedisCache(userIds);
    }

}
