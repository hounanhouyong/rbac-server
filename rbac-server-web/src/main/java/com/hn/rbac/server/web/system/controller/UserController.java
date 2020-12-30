package com.hn.rbac.server.web.system.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hn.rbac.server.common.annotation.Limit;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.common.result.Response;
import com.hn.rbac.server.service.*;
import com.hn.rbac.server.service.manager.UserManager;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.share.model.User;
import com.hn.rbac.server.share.model.UserRole;
import com.hn.rbac.server.share.request.enums.OrderByEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.share.request.query.MenuQuery;
import com.hn.rbac.server.share.request.query.SortModel;
import com.hn.rbac.server.share.request.query.UserQuery;
import com.hn.rbac.server.web.common.annotation.Log;
import com.hn.rbac.server.web.common.controller.BaseController;
import com.hn.rbac.server.web.common.exception.SystemException;
import com.hn.rbac.server.web.system.constants.SystemConstant;
import com.hn.rbac.server.web.system.request.UserRequest;
import com.hn.rbac.server.web.system.vo.MenuVO;
import com.hn.rbac.server.web.system.vo.UserProfileVO;
import com.hn.rbac.server.web.system.vo.UserVO;
import com.hn.rbac.server.web.system.vo.converter.MenuVOConverter;
import com.hn.rbac.server.web.system.vo.converter.UserVOConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserManager userManager;
    @Resource
    private UserQueryService userQueryService;
    @Resource
    private UserCommandService userCommandService;
    @Resource
    private UserRoleQueryService userRoleQueryService;
    @Resource
    private RoleQueryService roleQueryService;
    @Resource
    private RoleMenuQueryService roleMenuQueryService;
    @Resource
    private MenuQueryService menuQueryService;

    @Log("获取用户个人信息")
    @GetMapping("/profile")
    public Response profile() {
        // user
        String username = this.getCurrentUsername();
        User user = userQueryService.findByName(username);
        if (user == null) {
            return new Response().success();
        }
        // no return password
        user.setPassword(null);
        if (user.getStatus().equals(StatusEnum.INVALID.getValue())) {
            return new Response().success(UserProfileVO.builder().user(user).roles(new ArrayList<>()).menus(new ArrayList<>()).build());
        }

        // menu
        List<MenuVO> menus = new ArrayList<>();

        List<Long> roleIds = userRoleQueryService.findRoleIdsByUserId(user.getId());
        List<Role> roleList = roleQueryService.findValidRolesByBatchIds(roleIds);
        if (CollectionUtils.isEmpty(roleList)) {
            // 无角色情况
            return new Response().success(UserProfileVO.builder().user(user).roles(new ArrayList<>()).menus(new ArrayList<>()).build());
        }

        // role
        List<Role> roles = roleList;

        // query
        MenuQuery menuQuery = new MenuQuery();
        menuQuery.setStatus(StatusEnum.VALID.getValue());
        menuQuery.setPageNo(0);
        menuQuery.setPageSize(5000);
        // sort
        List<SortModel> sortModels = new ArrayList<>();
        sortModels.add(new SortModel("order_num", OrderByEnum.ASC.getValue()));

        // superAdmin 检查
        if (roleQueryService.hasValidSuperAdminRole(roleIds)) {
            // is superAdmin, so return all menus
            PageInfo<Menu> menuPageInfo = menuQueryService.findMenuPage(menuQuery, sortModels);
            menus = MenuVOConverter.INSTANCE.from(menuPageInfo.getData());
        } else {
            List<Long> validRoleIdList = roles.stream().map(Role::getId).collect(Collectors.toList());
            List<Long> menuIds = roleMenuQueryService.findMenuIdsByBatchRoleIds(validRoleIdList);

            if (CollectionUtils.isNotEmpty(menuIds)) {
                menuQuery.setBatchIds(menuIds);
                PageInfo<Menu> menuPageInfo = menuQueryService.findMenuPage(menuQuery, sortModels);
                menus = MenuVOConverter.INSTANCE.from(menuPageInfo.getData());
            }
        }

        // 无菜单情况
        if (CollectionUtils.isEmpty(menus)) {
            return new Response().success(UserProfileVO.builder().user(user).roles(roles).menus(new ArrayList<>()).build());
        }

        return new Response().success(UserProfileVO.builder().user(user).roles(roles).menus(menus).build());
    }

    @Log("查询用户信息")
    @GetMapping
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_USER_VIEW)
    public Response userList(UserQuery userQuery) {

        PageInfo<UserVO> userVOPageInfo = new PageInfo<>(userQuery.getPageNo(), userQuery.getPageSize());

        // sort
        List<SortModel> sortModels = new ArrayList<>();
        sortModels.add(new SortModel("id", OrderByEnum.DESC.getValue()));

        // userPage
        PageInfo<User> userPageInfo = userQueryService.findUserPage(userQuery, sortModels);
        if (CollectionUtils.isEmpty(userPageInfo.getData())) {
            return new Response().success(userVOPageInfo);
        }

        // userList
        List<User> userList = userPageInfo.getData();
        // userVOList
        List<UserVO> userVOList = new ArrayList<>();

        // user <-> role
        List<Long> userIdList = userList.stream().map(User::getId).distinct().collect(Collectors.toList());
        List<UserRole> userRoles = userRoleQueryService.findByBatchUserId(userIdList);
        if (CollectionUtils.isEmpty(userRoles)) {
            userVOList = UserVOConverter.INSTANCE.from(userList);
            userVOPageInfo.setData(userVOList);
            userVOPageInfo.setTotalCount(userPageInfo.getTotalCount());
            return new Response().success(userVOPageInfo);
        }

        // userId -> list<UserRole>
        Map<Long, List<UserRole>> userId2UserRoleMap = userRoles.stream().collect(Collectors.groupingBy(ur -> ur.getUserId()));

        for (User user : userList) {
            UserVO userVO = UserVOConverter.INSTANCE.from(user);
            if (userId2UserRoleMap.containsKey(user.getId())) {
                List<UserRole> userRoleList = userId2UserRoleMap.get(user.getId());

                // List<UserRole> to List<Role>
                List<Long> roleIdList =
                        userRoleList.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList());

                List<Role> roleList = roleQueryService.findValidRolesByBatchIds(roleIdList);;

                // set roleList
                userVO.setRoleList(roleList);
            }
            userVOList.add(userVO);
        }

        userVOPageInfo.setData(userVOList);
        userVOPageInfo.setTotalCount(userPageInfo.getTotalCount());

        return new Response().success(userVOPageInfo);
    }

    /**
     * 根据username模糊查询
     * @param username
     * @return
     */
    @Limit(key = "findEmployee", period = 60, count = 60, name = "查询员工信息接口", prefix = "limit")
    @Log("查询员工信息")
    @GetMapping("/findEmployee")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_USER_EMPLOYEE_VIEW)
    public Response findEmployee(String username) {
        // TODO 对接员工系统
        //
        return new Response().success();
    }

    @GetMapping("check/{username}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return userQueryService.findByName(username) == null;
    }

    @Log("新增用户")
    @PostMapping
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_USER_ADD)
    public Response addUser(@RequestBody UserRequest request) throws SystemException {
        Assert.isTrue(request != null && StringUtils.isNotEmpty(request.getUsername()), "Null username");
        if (userQueryService.findByName(request.getUsername()) != null) {
            throw new SystemException(String.format(SystemConstant.MSG_USER_NAME_EXIST, request.getUsername()));
        }
        try {
            // TODO
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_ADD_USER_FAIL, e);
            throw new SystemException(SystemConstant.MSG_ADD_USER_FAIL);
        }
    }

    @Log("禁用用户")
    @PutMapping("/disable/{userId}")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_USER_DISABLE)
    public Response disabledUser(@NotBlank(message = "{required}") @PathVariable String userId) throws SystemException {
        try {
            userCommandService.updateUserStatus(Long.parseLong(userId), StatusEnum.INVALID);
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_DISABLED_USER_FAIL, e);
            throw new SystemException(SystemConstant.MSG_DISABLED_USER_FAIL);
        }
    }

    @Log("启用用户")
    @PutMapping("/enable/{userId}")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_USER_ENABLE)
    public Response enabledUser(@NotBlank(message = "{required}") @PathVariable String userId) throws SystemException {
        try {
            userCommandService.updateUserStatus(Long.parseLong(userId), StatusEnum.VALID);
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_ENABLED_USER_FAIL, e);
            throw new SystemException(SystemConstant.MSG_ENABLED_USER_FAIL);
        }
    }

    @Log("修改用户")
    @PutMapping("/update")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_USER_UPDATE)
    public Response updateUser(@RequestBody UserRequest request) throws SystemException {
        // check
        this.noAllowModifyOwnRoleExcludeSuperAdmin(request.getId());
        try {
            userCommandService.updateUser(request.getId(), request.getRoleIds());
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_UPDATE_USER_FAIL, e);
            throw new SystemException(SystemConstant.MSG_UPDATE_USER_FAIL);
        }
    }

    @PutMapping("/deleteCache/{username}")
    public Response deleteCache(@NotBlank(message = "{required}") @PathVariable String username) throws SystemException {
        User user = userQueryService.findByName(username);
        if (user == null) {
            return new Response().success();
        }
        try {
            userManager.deleteUserRedisCache(username);
        } catch (Exception e) {
            log.error(SystemConstant.MSG_DELETE_USER_CACHE_FAIL, e);
            throw new SystemException(SystemConstant.MSG_DELETE_USER_CACHE_FAIL);
        }
        return new Response().success();
    }

    @PutMapping("/clearCache")
    public Response clearCache() throws SystemException {
        List<String> usernameList = userQueryService.findAllUsername();
        if (CollectionUtils.isNotEmpty(usernameList)) {
            for (String username : usernameList) {
                try {
                    userManager.deleteUserRedisCache(username);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response().fail();
                }
            }
        }
        return new Response().success();
    }

    /**
     * 不允许用户自己修改自己的角色，除超级管理员
     * @param userId
     */
    private void noAllowModifyOwnRoleExcludeSuperAdmin(Long userId) {
        Assert.isTrue(userId != null, "Null userId");
        User user = userQueryService.findById(userId);
        if (user == null) {
            throw new SystemException(String.format(SystemConstant.MSG_USER_NOT_EXIST, userId));
        }
        if (user.getStatus().equals(StatusEnum.INVALID.getValue())) {
            throw new SystemException(String.format(SystemConstant.MSG_USER_ALREADY_DISABLED, user.getUsername()));
        }
        List<Long> roleIds = userRoleQueryService.findRoleIdsByUserId(userId);
        if (!roleQueryService.hasValidSuperAdminRole(roleIds)) {
            String currentUsername = this.getCurrentUsername();
            if (StringUtils.isNotEmpty(currentUsername) && currentUsername.equals(user.getUsername())) {
                throw new SystemException(String.format(SystemConstant.MSG_USER_NOT_ALLOW_MODIFY_OWN_ROLE, user.getUsername()));
            }
        }
    }

}
