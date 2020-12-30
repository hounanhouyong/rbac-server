package com.hn.rbac.server.web.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.common.result.Response;
import com.hn.rbac.server.service.MenuCommandService;
import com.hn.rbac.server.service.MenuFunctionCommandService;
import com.hn.rbac.server.service.MenuFunctionQueryService;
import com.hn.rbac.server.service.MenuQueryService;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.model.MenuFunction;
import com.hn.rbac.server.share.request.enums.MenuTypeEnum;
import com.hn.rbac.server.share.request.enums.OrderByEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.share.request.query.MenuQuery;
import com.hn.rbac.server.share.request.query.SortModel;
import com.hn.rbac.server.web.common.annotation.Log;
import com.hn.rbac.server.web.common.exception.SystemException;
import com.hn.rbac.server.web.system.constants.SystemConstant;
import com.hn.rbac.server.web.system.request.MenuRequest;
import com.hn.rbac.server.web.system.vo.MenuBasicVO;
import com.hn.rbac.server.web.system.vo.MenuVO;
import com.hn.rbac.server.web.system.vo.converter.MenuBasicVOConverter;
import com.hn.rbac.server.web.system.vo.converter.MenuVOConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuQueryService menuQueryService;
    @Resource
    private MenuCommandService menuCommandService;
    @Resource
    private MenuFunctionQueryService menuFunctionQueryService;
    @Resource
    private MenuFunctionCommandService menuFunctionCommandService;

    @Log("查看菜单")
    @GetMapping
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_MENU_VIEW)
    public Response menuList(MenuQuery menuQuery) {

        PageInfo<MenuVO> menuVOPageInfo = new PageInfo<>(menuQuery.getPageNo(), menuQuery.getPageSize());

        // sort
        List<SortModel> sortModels = new ArrayList<>();
        sortModels.add(new SortModel("id", OrderByEnum.DESC.getValue()));

        // menuPage
        PageInfo<Menu> menuPageInfo = menuQueryService.findMenuPage(menuQuery, sortModels);
        if (CollectionUtils.isEmpty(menuPageInfo.getData())) {
            return new Response().success(menuVOPageInfo);
        }

        // menuList
        List<Menu> menuList = menuPageInfo.getData();

        // parentMenuList
        List<Long> parentMenuIds = menuList.stream().map(Menu::getParentId).distinct().collect(Collectors.toList());
        List<Menu> parentMenus = menuQueryService.findMenusByBatchIds(parentMenuIds);

        // map<menuId, menuName>
        Map<Long, String> menuId2nameMap = parentMenus.stream().collect(Collectors.toMap(Menu::getId, Menu::getMenuName));

        // menuId -> List<MenuFunction>
        List<Long> menuIds = menuList.stream().map(Menu::getId).distinct().collect(Collectors.toList());
        List<MenuFunction> menuFunctionList = menuFunctionQueryService.findByBatchMenuIds(menuIds);
        Map<Long, List<MenuFunction>> menuId2MenuFunctionsMap = new HashMap<>(16);
        if (!CollectionUtils.isEmpty(menuFunctionList)) {
            menuId2MenuFunctionsMap = menuFunctionList.stream().collect(Collectors.groupingBy(menuFunction -> menuFunction.getMenuId()));
        }

        // menuVOList
        List<MenuVO> menuVOList = new ArrayList<>();
        for (Menu menu : menuList) {
            MenuVO menuVO = MenuVOConverter.INSTANCE.from(menu);
            if (menuId2nameMap.containsKey(menu.getParentId())) {
                menuVO.setParenMenuName(menuId2nameMap.get(menu.getParentId()));
            }
            if (menuId2MenuFunctionsMap.containsKey(menu.getId())) {
                List<MenuFunction> menuFunctions = menuId2MenuFunctionsMap.get(menu.getId());
                List<Long> functionIds = menuFunctions.stream().map(MenuFunction::getFunctionId).distinct().collect(Collectors.toList());
                menuVO.setFunctionIds(functionIds);
            }
            menuVOList.add(menuVO);
        }

        // menuVOPage
        menuVOPageInfo.setData(menuVOList);
        menuVOPageInfo.setTotalCount(menuPageInfo.getTotalCount());

        return new Response().success(menuVOPageInfo);
    }


    @Log("查看菜单基本信息列表")
    @GetMapping("/basic")
    public Response menuBasicInfoList(MenuQuery menuQuery) {

        PageInfo<MenuBasicVO> menuBasicVOPageInfo = new PageInfo<>(menuQuery.getPageNo(), menuQuery.getPageSize());

        // sort
        List<SortModel> sortModels = new ArrayList<>();
        sortModels.add(new SortModel("order_num", OrderByEnum.ASC.getValue()));

        // menuPage
        PageInfo<Menu> menuPageInfo = menuQueryService.findMenuPage(menuQuery, sortModels);
        if (CollectionUtils.isEmpty(menuPageInfo.getData())) {
            return new Response().success(menuBasicVOPageInfo);
        }

        menuBasicVOPageInfo.setData(
                MenuBasicVOConverter.INSTANCE.from(menuQueryService.findMenuPage(menuQuery, sortModels).getData()));
        menuBasicVOPageInfo.setTotalCount(menuPageInfo.getTotalCount());

        return new Response().success(menuBasicVOPageInfo);
    }


    @Log("新增菜单")
    @PostMapping
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_MENU_ADD)
    public Response addMenu(@RequestBody MenuRequest menuRequest) throws SystemException {
        Assert.isTrue(menuRequest != null && !StringUtils.isEmpty(menuRequest.getMenuName()), "Null menuName");
        Assert.isTrue(!StringUtils.isEmpty(menuRequest.getType()), "Null type");
        // 验证菜单名称重复问题
        commonCheckMenuName(menuRequest.getType(), menuRequest.getParentId(), null, menuRequest.getMenuName());
        // 一级菜单URL必空验证
        checkOneLevelMenuUrlMustBeNull(menuRequest.getType(), menuRequest.getParentId(), menuRequest.getUrl());
        try {
            Long menuId = menuCommandService.createMenu(menuRequest.buildMenu());
            // 排序
            if (!StringUtils.isEmpty(menuRequest.getSortedIds())) {
                String[] menuIdArray = menuRequest.getSortedIds().split(StringPool.COMMA);
                menuCommandService.updateMenuOrderNum(menuIdArray, "-1", menuId.toString());
            }
            // 绑定接口
            if (!StringUtils.isEmpty(menuRequest.getFunctionIds())) {
                String[] functionIdArray = menuRequest.getFunctionIds().split(StringPool.COMMA);
                menuFunctionCommandService.saveMenuFunctions(menuId, functionIdArray);
            }
            return new Response().success(menuId);
        } catch (Exception e) {
            log.error(SystemConstant.MSG_ADD_MENU_FAIL, e);
            throw new SystemException(SystemConstant.MSG_ADD_MENU_FAIL);
        }
    }

    /**
     * 禁用菜单功能：禁用当前菜单下面所有的下级子菜单
     * @param menuId
     * @return
     * @throws SystemException
     */
    @Log("禁用菜单")
    @PutMapping("/disable/{menuId}")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_MENU_DISABLE)
    public Response disabledMenu(@NotBlank(message = "{required}") @PathVariable String menuId) throws SystemException {
        List<Long> menuIdList = menuQueryService.findAllSubMenuIdList(Long.parseLong(menuId));
        try {
            if (CollectionUtils.isEmpty(menuIdList)) {
                menuCommandService.updateMenuStatus(Long.parseLong(menuId), StatusEnum.INVALID);
            } else {
                menuIdList.add(Long.parseLong(menuId));
                menuCommandService.updateMenusStatus(menuIdList, StatusEnum.INVALID);
            }
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_DISABLED_MENU_FAIL, e);
            throw new SystemException(SystemConstant.MSG_DISABLED_MENU_FAIL);
        }
    }

    /**
     * 启用菜单功能，启用当前菜单下面所有的子菜单
     * @param menuId
     * @return
     * @throws SystemException
     */
    @Log("启用菜单")
    @PutMapping("/enable/{menuId}")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_MENU_ENABLE)
    public Response enabledMenu(@NotBlank(message = "{required}") @PathVariable String menuId) throws SystemException {
        List<Long> menuIdList = menuQueryService.findAllSubMenuIdList(Long.parseLong(menuId));
        try {
            if (CollectionUtils.isEmpty(menuIdList)) {
                menuCommandService.updateMenuStatus(Long.parseLong(menuId), StatusEnum.VALID);
            } else {
                menuIdList.add(Long.parseLong(menuId));
                menuCommandService.updateMenusStatus(menuIdList, StatusEnum.VALID);
            }
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_ENABLED_MENU_FAIL, e);
            throw new SystemException(SystemConstant.MSG_ENABLED_MENU_FAIL);
        }
    }

    @Log("修改菜单")
    @PutMapping("/update")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_MENU_UPDATE)
    public Response updateMenu(@RequestBody MenuRequest menuRequest) throws SystemException {

        // 验证是否存在, 不存在，直接返回成功
        Menu target = menuQueryService.findValidMenuById(menuRequest.getId());
        if (target == null) {
            return new Response().success();
        }

        // 验证菜单名称重复问题
        commonCheckMenuName(target.getType(), menuRequest.getParentId(), menuRequest.getId(), menuRequest.getMenuName());

        // 一级菜单URL必空验证
        Long parentId = menuRequest.getParentId() != null ? menuRequest.getParentId() : target.getParentId();
        checkOneLevelMenuUrlMustBeNull(menuRequest.getType(), parentId, menuRequest.getUrl());

        try {
            Menu menu = menuRequest.buildMenu();
            // 不支持相关数据项修改
            menu.setType(null);
            // 更新数据
            menuCommandService.updateMenu(menu, menuRequest.getFunctionIds());
            // 控制排序
            if (!StringUtils.isEmpty(menuRequest.getSortedIds())) {
                String[] menuIdArray = menuRequest.getSortedIds().split(StringPool.COMMA);
                menuCommandService.updateMenuOrderNum(menuIdArray, null, null);
            }
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_UPDATE_MENU_FAIL, e);
            throw new SystemException(SystemConstant.MSG_UPDATE_MENU_FAIL);
        }
    }

    @Log("绑定接口权限")
    @PutMapping("/bindFunction")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_MENU_BIND_FUNCTION)
    public Response bindFunction(@RequestBody MenuRequest menuRequest) throws SystemException {
        Assert.isTrue(menuRequest != null && menuRequest.getId() != null, "Null menuId");
        try {
            menuCommandService.bindFunctions(menuRequest.getId(), menuRequest.getFunctionIds());
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_MENU_BIND_FUNCTION_FAIL, e);
            throw new SystemException(SystemConstant.MSG_MENU_BIND_FUNCTION_FAIL);
        }
    }

    /**
     * 验证菜单名称重复问题
     * 情况一：type等于app, 所有type = app的记录中验证
     * 情况二：type等于function，相同上级下面所有记录中验证
     * 情况三：type等于menu或page，同APP下面所有记录中验证
     *
     * 特殊情况：修改菜单操作，原样上传菜单名的情况，忽略处理
     * @param parentId
     * @param menuId
     * @param menuName
     */
    private void commonCheckMenuName(String type, Long parentId, Long menuId, String menuName) {
        if (StringUtils.isEmpty(menuName)) {
            return;
        }
        if (MenuTypeEnum.FUNCTION.getValue().equals(type)) {
            this.checkFunctionName(menuId, menuName);
        } else {
            // type = app, 在所有应用中验证名称是否重复
            if (parentId == null || parentId.equals(AdminConstant.MENU_ROOT_ID)) {
                this.checkAppName(menuId, menuName);
            } else {
                this.checkMenuName(parentId, menuId, menuName);
            }
        }
    }

    private void checkAppName(Long menuId, String menuName) {
        Menu target = menuQueryService.findByParentIdAndName(AdminConstant.MENU_ROOT_ID, menuName);
        if (target != null) {
            if (menuId == null || !target.getId().equals(menuId)) {
                throw new SystemException(String.format(SystemConstant.MSG_MENU_NAME_EXIST, menuName));
            }
        }
    }

    private void checkFunctionName(Long menuId, String menuName) {
        Menu menu = menuQueryService.findValidMenuById(menuId);
        if (menu == null) {
            return;
        }
        Menu target = menuQueryService.findByParentIdAndName(menu.getParentId(), menuName);
        if (target != null) {
            if (menuId == null || !target.getId().equals(menuId)) {
                throw new SystemException(String.format(SystemConstant.MSG_MENU_NAME_EXIST, menuName));
            }
        }
    }

    private void checkMenuName(Long parentId, Long menuId, String menuName) {
        // 获取 应用ID
        Long appId;
        if (menuId == null) {
            // 新增操作验证菜单名称
            // 根据 parentId 找 appId
            appId = menuQueryService.findValidAppIdByMenuId(parentId);
        } else {
            // 修改操作验证菜单名称
            if (parentId == null) {
                // 1. 不需要调整父菜单
                // 根据 menuId 找 appId
                appId = menuQueryService.findValidAppIdByMenuId(menuId);
            } else {
                // 2. 需要调整父菜单
                // 根据 parentId 找 appId
                appId = menuQueryService.findValidAppIdByMenuId(parentId);
            }
        }

        // 在当前应用下验证名称是否重复
        List<Long> subMenuIdList = menuQueryService.findAllSubMenuIdList(appId);
        List<Menu> subMenus = menuQueryService.findMenusByBatchIds(subMenuIdList);
        if (!CollectionUtils.isEmpty(subMenus)) {
            List<Menu> existNameMenuList = subMenus.stream().filter(menu -> {
                if (menuId == null) {
                    return menu.getMenuName().equals(menuName);
                } else {
                    return menu.getMenuName().equals(menuName) && menuId != menu.getId();
                }
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(existNameMenuList)) {
                throw new SystemException(String.format(SystemConstant.MSG_MENU_NAME_EXIST, menuName));
            }
        }
    }

    /**
     * 验证一级菜单URL必空问题
     * @param menuType
     * @param parentId
     * @param url
     */
    private void checkOneLevelMenuUrlMustBeNull(String menuType, Long parentId, String url) {
        if (MenuTypeEnum.MENU.getValue().equals(menuType) && (parentId == null || parentId == AdminConstant.MENU_ROOT_ID) && !StringUtils.isEmpty(url)) {
            throw new SystemException(SystemConstant.MSG_NO_PARENT_MENU_URL_MUST_BE_NULL);
        }
    }

}
