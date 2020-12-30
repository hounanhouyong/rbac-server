package com.hn.rbac.server.web.system.controller;

import com.hn.rbac.server.auth.utils.TokenUtil;
import com.hn.rbac.server.common.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class LoginController {


    @GetMapping("logout")
    public Response logout(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;


        // TODO cookieName
        String cookieName = null;

        // TODO login url
        String loginUrl = null;

        String securityToken = TokenUtil.parsingSecurityToken(httpServletRequest, cookieName);
        if (StringUtils.isNotEmpty(securityToken)) {
            // TODO logout
            //
        }
        if (StringUtils.isNotEmpty(loginUrl) && StringUtils.isNotEmpty(httpServletRequest.getHeader("Referer"))) {
            loginUrl += "?redirect=" +  httpServletRequest.getHeader("Referer");
        }
        return new Response().success(loginUrl);
    }

}
