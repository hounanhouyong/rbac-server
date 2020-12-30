package com.hn.rbac.server.web.common.aspect;

import com.hn.rbac.server.auth.constants.AuthConstant;
import com.hn.rbac.server.auth.utils.JWTUtil;
import com.hn.rbac.server.common.context.RequestContext;
import com.hn.rbac.server.common.utils.HttpContextUtil;
import com.hn.rbac.server.common.utils.IPUtil;
import com.hn.rbac.server.service.MenuQueryService;
import com.hn.rbac.server.service.OpLogCommandService;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.model.OperationLog;
import com.hn.rbac.server.web.common.annotation.Log;
import com.hn.rbac.server.web.common.properties.OpLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * AOP 记录用户操作日志
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private OpLogCommandService opLogCommandService;
    @Resource
    private MenuQueryService menuQueryService;

    @Resource
    private OpLogProperties opLogProperties;

    @Pointcut("@annotation(com.hn.rbac.server.web.common.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP 地址
        String ip = IPUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long costTimes = System.currentTimeMillis() - beginTime;
        if (opLogProperties.isOpenAopLog()) {
            // 保存日志
            String username = null;
            String token = (String) SecurityUtils.getSubject().getPrincipal();
            if (!StringUtils.isEmpty(token)) {
                username = JWTUtil.getUsername(token);
            }
            Date now = new Date();
            OperationLog opLog = new OperationLog();
            opLog.setUsername(username);
            opLog.setIp(ip);
            opLog.setGmtCreate(now);
            opLog.setGmtModified(now);
            opLog.setCostTimes(costTimes);
//            opLog.setLocation(AddressUtil.getCityInfo(ip));

            // method
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            Log logAnnotation = method.getAnnotation(Log.class);
            if (logAnnotation != null) {
                // 注解上的描述
                opLog.setOperation(logAnnotation.value());

                String menuUrl = (String) RequestContext.get(AuthConstant.CONTEXT_KEY_REQUEST_MODULE);
                opLog.setAccessFrom(menuUrl);

                Menu menu = menuQueryService.findByUrl(menuUrl);
                if (menu != null) {
                    opLog.setModuleId(menu.getId());
                    opLog.setModuleName(menu.getMenuName());
                }
            }

            // 请求的类名
            String className = point.getTarget().getClass().getName();
            // 请求的方法名
            String methodName = signature.getName();
            opLog.setMethod(className + "." + methodName + "()");
            // 请求的方法参数值
            Object[] args = point.getArgs();
            // 请求的方法参数名称
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);

            opLogCommandService.saveLog(opLog, args, paramNames);
        }
        return result;
    }
}
