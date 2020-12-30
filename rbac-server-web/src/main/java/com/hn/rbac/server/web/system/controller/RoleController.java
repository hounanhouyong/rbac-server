package com.hn.rbac.server.web.system.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hn.rbac.server.common.result.Response;
import com.hn.rbac.server.service.MenuQueryService;
import com.hn.rbac.server.service.RoleCommandService;
import com.hn.rbac.server.service.RoleMenuQueryService;
import com.hn.rbac.server.service.RoleQueryService;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.share.model.RoleMenu;
import com.hn.rbac.server.share.request.enums.OrderByEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.share.request.query.RoleQuery;
import com.hn.rbac.server.share.request.query.SortModel;
import com.hn.rbac.server.web.common.annotation.Log;
import com.hn.rbac.server.web.common.exception.SystemException;
import com.hn.rbac.server.web.system.constants.SystemConstant;
import com.hn.rbac.server.web.system.request.RoleRequest;
import com.hn.rbac.server.web.system.vo.RoleMenuVO;
import com.hn.rbac.server.web.system.vo.RoleMenusVO;
import com.hn.rbac.server.web.system.vo.converter.RoleMenuVOConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleQueryService roleQueryService;
    @Resource
    private RoleCommandService roleCommandService;
    @Resource
    private RoleMenuQueryService roleMenuQueryService;
    @Resource
    private MenuQueryService menuQueryService;

    @Log("查询角色列表")
    @GetMapping
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_ROLE_VIEW)
    public Response roleList(RoleQuery roleQuery) {
        List<SortModel> sortModels = new ArrayList<>();
        sortModels.add(new SortModel("id", OrderByEnum.DESC.getValue()));
        return new Response().success(roleQueryService.findRolePage(roleQuery, sortModels));
    }

    @GetMapping("menu/{roleId}")
    public Response getRoleMenus(@NotBlank(message = "{required}") @PathVariable String roleId) {
        // role
        Role role = roleQueryService.findValidRoleById(Long.parseLong(roleId));
        if (role == null) {
            log.info("Null role or Invalid role, roleId={}", roleId);
            return new Response().success();
        }

        // build
        RoleMenusVO roleMenusVO = new RoleMenusVO();
        roleMenusVO.setRoleId(role.getId());
        roleMenusVO.setRoleName(role.getRoleName());
        roleMenusVO.setRemark(role.getRemark());

        // menuList
        List<RoleMenu> roleMenuList = roleMenuQueryService.findByRoleId(role.getId());
        if (CollectionUtils.isEmpty(roleMenuList)) {
            return new Response().success(roleMenusVO);
        }
        List<Long> menuIds = roleMenuList.stream().map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());
        List<Menu> menuList = menuQueryService.findValidMenusByBatchIds(menuIds);
        if (CollectionUtils.isEmpty(menuList)) {
            return new Response().success(roleMenusVO);
        }

        // map
        Map<Long, Menu> menuId2parentIdsMap = menuList.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));

        List<RoleMenuVO> roleMenuVOList = new ArrayList<>();
        for (RoleMenu roleMenu : roleMenuList) {
            RoleMenuVO roleMenuVO = RoleMenuVOConverter.INSTANCE.from(roleMenu);
            if (menuId2parentIdsMap.containsKey(roleMenu.getMenuId())) {
                Menu menu = menuId2parentIdsMap.get(roleMenu.getMenuId());
                roleMenuVO.setMenuName(menu.getMenuName());
                roleMenuVO.setType(menu.getType());
                roleMenuVO.setUrl(menu.getUrl());
                roleMenuVO.setParentIds(menu.getParentIds());
            }
            roleMenuVOList.add(roleMenuVO);
        }

        // build
        roleMenusVO.setMenuList(roleMenuVOList);
        return new Response().success(roleMenusVO);
    }

    @Log("新增角色")
    @PostMapping
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_ROLE_ADD)
    public Response addRole(@RequestBody RoleRequest request) throws SystemException {
        Assert.isTrue(request != null && !StringUtils.isEmpty(request.getRoleName()), "Null roleName");
        if (roleQueryService.findByName(request.getRoleName()) != null) {
            throw new SystemException(String.format(SystemConstant.MSG_ROLE_NAME_EXIST, request.getRoleName()));
        }
        try {
            Long roleId = roleCommandService.createRole(request.buildRole(), request.getMenuIds());
            return new Response().success(roleId);
        } catch (Exception e) {
            log.error(SystemConstant.MSG_ADD_ROLE_FAIL, e);
            throw new SystemException(SystemConstant.MSG_ADD_ROLE_FAIL);
        }
    }

    @Log("禁用角色")
    @PutMapping("/disable/{roleId}")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_ROLE_DISABLE)
    public Response disabledRole(@NotBlank(message = "{required}") @PathVariable String roleId) throws SystemException {
        try {
            roleCommandService.updateRoleStatus(Long.parseLong(roleId), StatusEnum.INVALID);
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_DISABLED_ROLE_FAIL, e);
            throw new SystemException(SystemConstant.MSG_DISABLED_ROLE_FAIL);
        }
    }

    @Log("启用角色")
    @PutMapping("/enable/{roleId}")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_ROLE_ENABLE)
    public Response enabledRole(@NotBlank(message = "{required}") @PathVariable String roleId) throws SystemException {
        try {
            roleCommandService.updateRoleStatus(Long.parseLong(roleId), StatusEnum.VALID);
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_ENABLED_ROLE_FAIL, e);
            throw new SystemException(SystemConstant.MSG_ENABLED_ROLE_FAIL);
        }
    }

    @Log("修改角色")
    @PutMapping("/update")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_ROLE_UPDATE)
    public Response updateRole(@RequestBody RoleRequest request) throws SystemException {
        Role targetRole = roleQueryService.findByName(request.getRoleName());
        if (targetRole != null && !targetRole.getId().equals(request.getId())) {
            throw new SystemException(String.format(SystemConstant.MSG_ROLE_NAME_EXIST, request.getRoleName()));
        }
        try {
            roleCommandService.updateRole(request.buildRole(), request.getMenuIds());
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_UPDATE_ROLE_FAIL, e);
            throw new SystemException(SystemConstant.MSG_UPDATE_ROLE_FAIL);
        }
    }
}
