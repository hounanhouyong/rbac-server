package com.hn.rbac.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.service.FunctionQueryService;
import com.hn.rbac.server.service.MenuFunctionCommandService;
import com.hn.rbac.server.service.MenuQueryService;
import com.hn.rbac.server.service.PersistentLogCommandService;
import com.hn.rbac.server.service.dao.MenuFunctionMapper;
import com.hn.rbac.server.service.entity.MenuFunctionDO;
import com.hn.rbac.server.share.model.Function;
import com.hn.rbac.server.share.model.PersistentLog;
import com.hn.rbac.server.share.request.enums.PersistentObjTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuFunctionCommandServiceImpl extends ServiceImpl<MenuFunctionMapper, MenuFunctionDO> implements MenuFunctionCommandService {

    @Resource
    private MenuQueryService menuQueryService;
    @Resource
    private FunctionQueryService functionQueryService;
    @Resource
    private PersistentLogCommandService persistentLogCommandService;

    @Override
    public void saveMenuFunctions(Long menuId, String[] functionIds) {
        if (menuId == null) {
            return;
        }
        if (functionIds == null && functionIds.length == 0) {
            return;
        }
        // appId
        Long menuAppId = menuQueryService.findValidAppIdByMenuId(menuId);

        Date current = new Date();
        Arrays.stream(functionIds).forEach(functionId -> {
            Function function = functionQueryService.findValidFunctionById(Long.parseLong(functionId));
            if (function != null) {
                Long functionAppId = function.getAppId();
                // 是否是通用接口功能
                boolean isCommonFunction = functionAppId == null || functionAppId == AdminConstant.MENU_ROOT_ID;
                // 是通用接口功能 或者 接口功能的领域和菜单的领域相同，满足这两个条件之一才可以绑定
                if (isCommonFunction || functionAppId.equals(menuAppId)) {
                    MenuFunctionDO menuFunctionDO = new MenuFunctionDO();
                    menuFunctionDO.setGmtCreate(current);
                    menuFunctionDO.setGmtModified(current);
                    menuFunctionDO.setMenuId(menuId);
                    menuFunctionDO.setFunctionId(Long.parseLong(functionId));
                    baseMapper.insert(menuFunctionDO);

                    // 变更记录持久化
                    persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                                    .objType(PersistentObjTypeEnum.MENU_FUNCTION.getValue())
                                                                                    .objId(menuFunctionDO.getId())
                                                                                    .modifiedValue(JSON.toJSONString(menuFunctionDO))
                                                                                    .build());
                }
            }
        });
    }

    @Override
    public void deleteMenuFunctionsByMenuId(Long... menuIds) {
        if (menuIds == null) {
            return;
        }
        Arrays.stream(menuIds).forEach(id -> {
            baseMapper.delete(new LambdaQueryWrapper<MenuFunctionDO>().eq(MenuFunctionDO::getMenuId, id));
        });
    }
}
