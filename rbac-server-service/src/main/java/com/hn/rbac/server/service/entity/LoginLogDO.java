package com.hn.rbac.server.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("login_log")
public class LoginLogDO extends BaseDO {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 登录用户
     */
    private String username;
    /**
     * 登录地点
     */
    private String location;
    /**
     * 登录 IP
     */
    private String ip;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 登录浏览器
     */
    private String browser;

}
