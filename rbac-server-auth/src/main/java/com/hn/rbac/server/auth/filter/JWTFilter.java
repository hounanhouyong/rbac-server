package com.hn.rbac.server.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.rbac.server.auth.constants.AuthConstant;
import com.hn.rbac.server.auth.properties.AuthProperties;
import com.hn.rbac.server.auth.token.JWTToken;
import com.hn.rbac.server.auth.utils.JWTUtil;
import com.hn.rbac.server.auth.utils.TokenUtil;
import com.hn.rbac.server.cache.exception.RedisConnectException;
import com.hn.rbac.server.cache.service.RedisService;
import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.common.context.RequestContext;
import com.hn.rbac.server.common.result.Response;
import com.hn.rbac.server.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private static final String TOKEN = "Authentication";

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        AuthProperties authProperties = SpringContextUtil.getBean(AuthProperties.class);
        String[] anonUrl = StringUtils.splitByWholeSeparatorPreserveAllTokens(authProperties.getShiro().getAnonUrl(), ",");

        boolean match = false;
        for (String u : anonUrl) {
            if (pathMatcher.match(u, httpServletRequest.getRequestURI()))
                match = true;
        }
        if (match) {
            return true;
        }
        if (isLoginAttempt(request, response)) {
            return executeLogin(request, response);
        }
        return false;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        // 验证员工系统token
        if (StringUtils.isEmpty(parsingSecurityToken(request))) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        String securityToken = parsingSecurityToken(request);
        if (StringUtils.isEmpty(securityToken)) {
            return false;
        }

        // context
        RequestContext.put(AuthConstant.CONTEXT_KEY_SECURITY_TOKEN, securityToken);

        // TODO 获取当前登录用户
        //
        String username = null;

        // 缓存username和securityToken
        // redisService
        RedisService redisService = SpringContextUtil.getBean(RedisService.class);
        try {
            redisService.hset(AdminConstant.USER_SECURITY_TOKEN_HSET_KEY_NAME, username, securityToken);
        } catch (RedisConnectException e) {
            e.printStackTrace();
            log.error("缓存securityToken失败");
        }

        // context
        RequestContext.put(AuthConstant.CONTEXT_KEY_USERNAME, username);

        // 生成系统内token，并login
        String token = JWTUtil.sign(username, AdminConstant.DEFAULT_PASSWORD, securityToken);
        JWTToken jwtToken = new JWTToken(token);
        try {
            getSubject(request, response).login(jwtToken);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        // clear
        RequestContext.clear();

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Content-Type", "application/json");

        RequestContext.put(AuthConstant.CONTEXT_KEY_REQUEST_MODULE, httpServletRequest.getHeader("module"));

        // 跨域时会首先发送一个 option请求，这里我们给 option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        log.debug("Authentication required: sending 401 Authentication challenge response.");
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpResponse.getWriter()) {

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            // TODO 构造login url
            String loginUrl = null;

            /**
             * 情况1：无securityToken
             * 情况2：有securityToken，非本系统用户
             */
            String message = AuthConstant.MSG_NO_AUTH;
            Optional<Object> optional = Optional.ofNullable(RequestContext.get(AuthConstant.CONTEXT_KEY_SYSTEM_USER));
            if (optional.isPresent() && (int) optional.get() == AuthConstant.NO_SYSTEM_USER) {
                // logout
                String securityToken = (String) RequestContext.get(AuthConstant.CONTEXT_KEY_SECURITY_TOKEN);
                String username = (String) RequestContext.get(AuthConstant.CONTEXT_KEY_USERNAME);

                // TODO 登出

                // redisService
                RedisService redisService = SpringContextUtil.getBean(RedisService.class);
                try {
                    redisService.hdel(AdminConstant.USER_SECURITY_TOKEN_HSET_KEY_NAME, username);
                } catch (RedisConnectException e) {
                    e.printStackTrace();
                    log.error("清除securityToken失败");
                }
                log.info(String.format("logout, cookie=%s", securityToken));
                message = String.format(AuthConstant.MSG_NO_SYSTEM_USER, username);
            }

            // ObjectMapper
            ObjectMapper mapper = SpringContextUtil.getBean(ObjectMapper.class);
            Response responseObj = new Response().fail(HttpStatus.UNAUTHORIZED, message, loginUrl);
            String responseJson = mapper.writeValueAsString(responseObj);
            out.print(responseJson);
        } catch (IOException e) {
            log.error("sendChallenge error：", e);
        }
        return false;
    }

    /**
     * 解析securityToken
     * @param request
     * @return
     */
    private String parsingSecurityToken(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // TODO cookieName
        String cookieName = null;

        log.info("cookieName={}", cookieName);
        return TokenUtil.parsingSecurityToken(httpServletRequest, cookieName);
    }

}
