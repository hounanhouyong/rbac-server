package com.hn.rbac.server.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.rbac.server.cache.service.RedisService;
import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.service.CacheService;
import com.hn.rbac.server.service.FunctionQueryService;
import com.hn.rbac.server.service.RoleQueryService;
import com.hn.rbac.server.service.UserQueryService;
import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.share.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CacheServiceImpl implements CacheService {

    @Resource
    private RedisService redisService;
    @Resource
    private UserQueryService userQueryService;
    @Resource
    private RoleQueryService roleQueryService;
    @Resource
    private FunctionQueryService functionQueryService;

    @Resource
    private ObjectMapper mapper;

    private static final Long EXPIRE_TIME = 3600000L;

    @Override
    public void testConnect() throws Exception {
        this.redisService.exists("test");
    }

    @Override
    public User getUser(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        String userString = this.redisService.get(AdminConstant.USER_CACHE_PREFIX + username);
        if (StringUtils.isBlank(userString)) {
//            throw new Exception();
            return null;
        } else {
            return this.mapper.readValue(userString, User.class);
        }
    }

    @Override
    public List<Role> getRoles(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        String roleListString = this.redisService.get(AdminConstant.USER_ROLE_CACHE_PREFIX + username);
        if (StringUtils.isBlank(roleListString)) {
//            throw new Exception();
            return null;
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Role.class);
            return this.mapper.readValue(roleListString, type);
        }
    }

    @Override
    public List<String> getPermissions(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        String permissionListString = this.redisService.get(AdminConstant.USER_PERMISSION_CACHE_PREFIX + username);
        if (StringUtils.isBlank(permissionListString)) {
//            throw new Exception();
            return null;
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, String.class);
            return this.mapper.readValue(permissionListString, type);
        }
    }

    @Override
    public String getUserSecurityToken(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        return this.redisService.hget(AdminConstant.USER_SECURITY_TOKEN_HSET_KEY_NAME, username);
    }

    @Override
    public void saveUser(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        User user = userQueryService.findValidUserByName(username);
        this.deleteUser(username);
        if (user != null) {
            redisService.set(AdminConstant.USER_CACHE_PREFIX + username, mapper.writeValueAsString(user), EXPIRE_TIME);
        }
    }

    @Override
    public void saveRoles(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        List<Role> roleList = roleQueryService.findUserRole(username);
        this.deleteRoles(username);
        if (!CollectionUtils.isEmpty(roleList)) {
            redisService.set(AdminConstant.USER_ROLE_CACHE_PREFIX + username, mapper.writeValueAsString(roleList), EXPIRE_TIME);
        }

    }

    @Override
    public void savePermissions(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        List<String> permissionsList = functionQueryService.findFunctionCodeList(username);
        this.deletePermissions(username);
        if (!CollectionUtils.isEmpty(permissionsList)) {
            redisService.set(AdminConstant.USER_PERMISSION_CACHE_PREFIX + username, mapper.writeValueAsString(permissionsList), EXPIRE_TIME);
        }
    }

    @Override
    public void deleteUser(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        username = username.toLowerCase();
        redisService.del(AdminConstant.USER_CACHE_PREFIX + username);
    }

    @Override
    public void deleteRoles(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        username = username.toLowerCase();
        redisService.del(AdminConstant.USER_ROLE_CACHE_PREFIX + username);
    }

    @Override
    public void deletePermissions(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        username = username.toLowerCase();
        redisService.del(AdminConstant.USER_PERMISSION_CACHE_PREFIX + username);
    }

    @Override
    public void deleteUserSecurityToken(String username) throws Exception {
        if (StringUtils.isEmpty(username)) {
            return;
        }
        redisService.hdel(AdminConstant.USER_SECURITY_TOKEN_HSET_KEY_NAME, username);
    }

}