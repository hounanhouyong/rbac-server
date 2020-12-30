package com.hn.rbac.server.web.common.handler;

import com.hn.rbac.server.common.exception.LimitAccessException;
import com.hn.rbac.server.common.result.Response;
import com.hn.rbac.server.web.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Response handleException(Exception e) {
        log.error("系统内部异常，异常信息", e);
        return new Response().fail(HttpStatus.INTERNAL_SERVER_ERROR,"系统内部异常");
    }

    @ExceptionHandler(value = SystemException.class)
    public Response handleAdminException(SystemException e) {
        log.error("系统错误", e);
        return new Response().fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     */
    @ExceptionHandler(BindException.class)
    public Response validExceptionHandler(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new Response().fail(HttpStatus.BAD_REQUEST, message.toString());
    }

    /**
     * 统一处理请求参数校验(普通传参)
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Response handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new Response().fail(HttpStatus.BAD_REQUEST, message.toString());
    }

    @ExceptionHandler(value = LimitAccessException.class)
    public Response handleLimitAccessException(LimitAccessException e) {
        log.debug("LimitAccessException", e);
        return new Response().fail(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public Response handleUnauthorizedException(UnauthorizedException e) {
        log.debug("UnauthorizedException", e);
        return new Response().fail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public Response handleAuthenticationException(AuthenticationException e) {
        log.debug("AuthenticationException", e);
        return new Response().fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public Response handleAuthorizationException(AuthorizationException e){
        log.debug("AuthorizationException", e);
        return new Response().fail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(value = ExpiredSessionException.class)
    public Response handleExpiredSessionException(ExpiredSessionException e) {
        log.debug("ExpiredSessionException", e);
        return new Response().fail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

}
