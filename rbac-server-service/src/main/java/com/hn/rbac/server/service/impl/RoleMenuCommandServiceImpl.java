package com.hn.rbac.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.PersistentLogCommandService;
import com.hn.rbac.server.service.RoleMenuCommandService;
import com.hn.rbac.server.service.dao.RoleMenuMapper;
import com.hn.rbac.server.service.entity.RoleMenuDO;
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
public class RoleMenuCommandServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuDO> implements RoleMenuCommandService {

    @Resource
    private PersistentLogCommandService persistentLogCommandService;

    @Transactional
    @Override
    public void saveRoleMenus(Long roleId, String[] menuIds) {
        if (roleId == null) {
            return;
        }
        if (menuIds == null && menuIds.length == 0) {
            return;
        }
        Date current = new Date();
        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenuDO roleMenuDO = new RoleMenuDO();
            roleMenuDO.setGmtCreate(current);
            roleMenuDO.setGmtModified(current);
            roleMenuDO.setMenuId(Long.valueOf(menuId.split(StringPool.COLON)[0]));
            roleMenuDO.setAllSelected(Integer.parseInt(menuId.split(StringPool.COLON)[1]));
            roleMenuDO.setRoleId(roleId);
            baseMapper.insert(roleMenuDO);

            // 变更记录持久化
            persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                            .objType(PersistentObjTypeEnum.USER_ROLE.getValue())
                                                                            .objId(roleMenuDO.getId())
                                                                            .modifiedValue(JSON.toJSONString(roleMenuDO))
                                                                            .build());
        });
    }

    @Transactional
    @Override
    public void deleteRoleMenusByRoleId(Long... roleIds) {
        if (roleIds == null) {
            return;
        }
        Arrays.stream(roleIds).forEach(id -> {
            baseMapper.delete(new LambdaQueryWrapper<RoleMenuDO>().eq(RoleMenuDO::getRoleId, id));
        });
    }

    @Transactional
    @Override
    public void deleteRoleMenusByMenuId(Long... menuIds) {
        if (menuIds == null) {
            return;
        }
        Arrays.stream(menuIds).forEach(id -> {
            baseMapper.delete(new LambdaQueryWrapper<RoleMenuDO>().eq(RoleMenuDO::getMenuId, id));
        });
    }
}
