package com.hn.rbac.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.service.*;
import com.hn.rbac.server.service.dao.MenuMapper;
import com.hn.rbac.server.service.entity.MenuDO;
import com.hn.rbac.server.service.entity.converter.MenuConverter;
import com.hn.rbac.server.service.manager.UserManager;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.model.PersistentLog;
import com.hn.rbac.server.share.request.enums.MenuTypeEnum;
import com.hn.rbac.server.share.request.enums.PersistentObjTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuCommandServiceImpl extends ServiceImpl<MenuMapper, MenuDO> implements MenuCommandService {

    @Resource
    private UserManager userManager;
    @Resource
    private RoleMenuQueryService roleMenuQueryService;
    @Resource
    private UserRoleQueryService userRoleQueryService;
    @Resource
    private MenuQueryService menuQueryService;
    @Resource
    private MenuFunctionCommandService menuFunctionCommandService;
    @Resource
    private PersistentLogCommandService persistentLogCommandService;

    @Override
    public Long createMenu(Menu menu) {
        Assert.isTrue(MenuTypeEnum.getByValue(menu.getType()) != null, "No support menuType.");
        Date now = new Date();
        menu.setGmtCreate(now);
        menu.setGmtModified(now);
        menu.setStatus(StatusEnum.VALID.getValue());
        menu.setOrderNum(AdminConstant.DEFAULT_ORDER_NUM);

        if (menu.getParentId() == null) {
            menu.setParentId(AdminConstant.MENU_ROOT_ID);
            menu.setParentIds(null);
        } else {
            menu.setParentIds(this.buildParentIds(menu.getParentId()));
        }

        MenuDO menuDO = MenuConverter.INSTANCE.to(menu);
        this.save(menuDO);

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.MENU.getValue())
                                                                        .objId(menuDO.getId())
                                                                        .modifiedValue(JSON.toJSONString(menuDO))
                                                                        .build());

        return menuDO.getId();
    }

    @Transactional
    @Override
    public void updateMenu(Menu menu, String functionIds) throws Exception {
        Assert.isTrue(menu != null && menu.getId() != null, "Null menuId");
        // 是否要修正所有子菜单的parentIds字段
        boolean needUpdateAllSubMenusParentIds = false;

        if (menu.getParentId() != null) {
            needUpdateAllSubMenusParentIds = true;
        }

        // update menu
        menu.setGmtModified(new Date());

        if (needUpdateAllSubMenusParentIds) {
            menu.setParentIds(this.buildParentIds(menu.getParentId()));
        }

        this.baseMapper.updateById(MenuConverter.INSTANCE.to(menu));

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.MENU.getValue())
                                                                        .objId(menu.getId())
                                                                        .modifiedValue(JSON.toJSONString(menu))
                                                                        .build());

        if (needUpdateAllSubMenusParentIds) {
            // parentIds
            String parentIds;
            if (StringUtils.isNotEmpty(menu.getParentIds())) {
                parentIds = menu.getParentIds() + StringPool.COMMA + menu.getId();
            } else {
                parentIds = menu.getId().toString();
            }
            // all sub menus
            List<Long> allSubMenuIdList = menuQueryService.findAllSubMenuIdList(menu.getId());
            if (!CollectionUtils.isEmpty(allSubMenuIdList)) {
                allSubMenuIdList.stream().forEach(menuId -> {
                    MenuDO menuDO = new MenuDO();
                    menuDO.setId(menuId);
                    menuDO.setParentIds(parentIds);
                    this.baseMapper.updateById(menuDO);

                    // 变更记录持久化
                    persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                                    .objType(PersistentObjTypeEnum.MENU.getValue())
                                                                                    .objId(menuDO.getId())
                                                                                    .modifiedValue(JSON.toJSONString(menuDO))
                                                                                    .build());
                });
            }
        }

        // 绑定功能接口
//        this.bindFunctions(menu.getId(), functionIds);

        // reload user cache
        this.reloadUserCache(menu.getId());
    }

    @Override
    public void updateMenuStatus(Long menuId, StatusEnum statusEnum) throws Exception {
        Assert.isTrue(menuId != null, "Null menuId");
        MenuDO menuDO = new MenuDO();
        menuDO.setId(menuId);
        menuDO.setStatus(statusEnum.getValue());
        menuDO.setGmtModified(new Date());
        this.baseMapper.updateById(menuDO);

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.MENU.getValue())
                                                                        .objId(menuDO.getId())
                                                                        .modifiedValue(JSON.toJSONString(menuDO))
                                                                        .build());
    }

    @Transactional
    @Override
    public void updateMenusStatus(List<Long> menuIds, StatusEnum statusEnum) throws Exception {
        if (CollectionUtils.isEmpty(menuIds)) {
            return;
        }
        for (Long menuId : menuIds) {
            this.updateMenuStatus(menuId, statusEnum);
            // reload user cache
            this.reloadUserCache(menuId);
        }
    }

    @Transactional
    @Override
    public void updateMenuOrderNum(String[] menuIdArray, String targetVal, String replaceValue) throws Exception {
        if (menuIdArray == null || menuIdArray.length == 0) {
            return;
        }
        // 初始排序序号值
        Long[] initSortNum = {1L};

        Arrays.stream(menuIdArray).forEach(menuId -> {
            MenuDO menuDO = new MenuDO();
            if (StringUtils.isNotEmpty(targetVal) && StringUtils.isNotEmpty(replaceValue) && menuId.equals(targetVal)) {
                menuDO.setId(Long.parseLong(replaceValue));
            } else {
                menuDO.setId(Long.parseLong(menuId));
            }
            menuDO.setOrderNum(initSortNum[0]++);
            baseMapper.updateById(menuDO);

            // 变更记录持久化
            persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                            .objType(PersistentObjTypeEnum.MENU.getValue())
                                                                            .objId(menuDO.getId())
                                                                            .modifiedValue(JSON.toJSONString(menuDO))
                                                                            .build());
        });
    }

    @Override
    public void bindFunctions(Long menuId, String functionIds) throws Exception {
        // delete menu <-> function
        menuFunctionCommandService.deleteMenuFunctionsByMenuId(menuId);
        if (StringUtils.isNotEmpty(functionIds)) {
            // save menu <-> function
            String[] functionIdArray = functionIds.split(StringPool.COMMA);
            menuFunctionCommandService.saveMenuFunctions(menuId, functionIdArray);
        }
        // reload user cache
        this.reloadUserCache(menuId);
    }

    /**
     * 构建parentIds
     * @param parentId
     * @return
     */
    private String buildParentIds(Long parentId) {
        Menu parentMenu = menuQueryService.findValidMenuById(parentId);
        if (StringUtils.isNotEmpty(parentMenu.getParentIds())) {
            return parentMenu.getParentIds() + StringPool.COMMA + parentId;
        } else {
            return parentId.toString();
        }
    }

    private void reloadUserCache(Long menuId) throws Exception {
        // menu <-> role
        List<Long> roleIds = roleMenuQueryService.findRoleIdsByMenuId(menuId);
        // role <-> user
        List<Long> userIds = userRoleQueryService.findUserIdsByBatchRoleIds(roleIds);
        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserRedisCache(userIds);
    }

}
