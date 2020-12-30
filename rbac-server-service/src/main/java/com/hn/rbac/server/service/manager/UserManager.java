package com.hn.rbac.server.service.manager;

import com.hn.rbac.server.cache.utils.CacheUtil;
import com.hn.rbac.server.service.CacheService;
import com.hn.rbac.server.service.FunctionQueryService;
import com.hn.rbac.server.service.RoleQueryService;
import com.hn.rbac.server.service.UserQueryService;
import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.share.model.User;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 封装一些和 User相关的业务操作
 */
@Service
public class UserManager {

    @Resource
    private CacheService cacheService;
    @Resource
    private UserQueryService userQueryService;
    @Resource
    private RoleQueryService roleQueryService;
    @Resource
    private FunctionQueryService functionQueryService;


    /**
     * 通过用户名获取用户基本信息
     *
     * @param username 用户名
     * @return 用户基本信息
     */
    public User getUser(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        return CacheUtil.selectCacheByTemplate(
                () -> cacheService.getUser(username),
                () -> userQueryService.findByName(username));
    }

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    public Set<String> getUserRoles(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        List<Role> roleList = CacheUtil.selectCacheByTemplate(
                () -> cacheService.getRoles(username),
                () -> roleQueryService.findUserRole(username));
        if (CollectionUtils.isEmpty(roleList)) {
            return new HashSet<>();
        }
        return roleList.stream().map(Role::getRoleName).collect(Collectors.toSet());
    }

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    public Set<String> getUserPermissions(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        List<String> permissionList = CacheUtil.selectCacheByTemplate(
                () -> cacheService.getPermissions(username),
                () -> functionQueryService.findFunctionCodeList(username));
        if (CollectionUtils.isEmpty(permissionList)) {
            return new HashSet<>();
        }
        return permissionList.stream().collect(Collectors.toSet());
    }

    /**
     * 加载用户缓存信息到 Redis缓存中
     * @param username
     * @throws Exception
     */
    public void initUserRedisCache(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        // 缓存用户
        cacheService.saveUser(username);
        // 缓存用户角色
        cacheService.saveRoles(username);
        // 缓存用户权限
        cacheService.savePermissions(username);
    }

    /**
     * 将用户相关信息添加到 Redis缓存中
     *
     * @param username
     */
    public void loadUserRedisCache(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        this.initUserRedisCache(username);
        //踢掉user
        this.kickOff(username);
    }

    /**
     * 将用户相关信息添加到 Redis缓存中
     *
     * @param userIds userIds
     */
    public void loadUserRedisCache(List<Long> userIds) throws Exception {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        List<User> users = userQueryService.findByBatchIds(userIds);
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        for (User user : users) {
            this.loadUserRedisCache(user.getUsername());
        }
    }

    /**
     * 通过用户 id集合批量删除用户 Redis缓存
     *
     * @param username
     */
    public void deleteUserRedisCache(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        cacheService.deleteUser(username);
        cacheService.deleteRoles(username);
        cacheService.deletePermissions(username);
    }

    public void kickOff(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        String securityToken = cacheService.getUserSecurityToken(username);
        if (!StringUtils.isEmpty(securityToken)) {
            // TODO logout
            //
            cacheService.deleteUserSecurityToken(username);
        }
    }

}
