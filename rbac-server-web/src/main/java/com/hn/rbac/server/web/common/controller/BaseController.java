package com.hn.rbac.server.web.common.controller;

import com.hn.rbac.server.auth.utils.JWTUtil;
import com.hn.rbac.server.service.UserQueryService;
import com.hn.rbac.server.service.manager.UserManager;
import com.hn.rbac.server.share.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

public class BaseController {

    @Resource
    private UserQueryService userQueryService;
    @Resource
    private UserManager userManager;

    private Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前访问用户
     */
    protected String getCurrentUsername() {
        String token = (String) this.getSubject().getPrincipal();
        if (!StringUtils.isEmpty(token)) {
            return JWTUtil.getUsername(token);
        }
        return null;
    }

    /**
     * 获取当前访问用户
     */
    protected User getCurrentUser() {
        String token = (String) this.getSubject().getPrincipal();
        if (!StringUtils.isEmpty(token)) {
            String username = JWTUtil.getUsername(token);
            return userQueryService.findValidUserByName(username);
        }
        return null;
    }

    protected Session getSession() {
        return getSubject().getSession();
    }

    /**
     * 设置cookie无效
     */
    protected void setCookieInvalidByUsername(String username) {
        try {
            userManager.kickOff(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

}
