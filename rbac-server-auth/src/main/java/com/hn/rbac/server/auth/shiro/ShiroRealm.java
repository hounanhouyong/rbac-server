package com.hn.rbac.server.auth.shiro;

import com.hn.rbac.server.auth.constants.AuthConstant;
import com.hn.rbac.server.auth.token.JWTToken;
import com.hn.rbac.server.auth.utils.JWTUtil;
import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.common.context.RequestContext;
import com.hn.rbac.server.service.manager.UserManager;
import com.hn.rbac.server.share.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 */
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private UserManager userManager;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 授权模块，获取用户角色和权限
     *
     * @param token token
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
        String username = JWTUtil.getUsername(token.toString());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色集
        Set<String> roleSet = userManager.getUserRoles(username);
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限集
        Set<String> permissionSet = userManager.getUserPermissions(username);
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param authenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        String username = JWTUtil.getUsername(token);
        String st = JWTUtil.getSt(token);

        if (StringUtils.isBlank(username)) {
            throw new AuthenticationException(AuthConstant.MSG_TOKEN_AUTH_NO_PASS);
        }

        // 通过用户名查询用户信息
        User user = userManager.getUser(username);
        if (user == null) {
            // context
            RequestContext.put(AuthConstant.CONTEXT_KEY_SYSTEM_USER, AuthConstant.NO_SYSTEM_USER);
            throw new AuthenticationException(String.format(AuthConstant.MSG_NO_SYSTEM_USER, username));
        }
        // context
        RequestContext.put(AuthConstant.CONTEXT_KEY_SYSTEM_USER, AuthConstant.SYSTEM_USER);

        // 校验
        if (!JWTUtil.verify(token, username, AdminConstant.DEFAULT_PASSWORD, st)) {
            throw new AuthenticationException(AuthConstant.MSG_TOKEN_AUTH_NO_PASS);
        }
        return new SimpleAuthenticationInfo(token, token, "admin_shiro_realm");
    }
}
