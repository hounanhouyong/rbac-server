package com.hn.rbac.server.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("operation_log")
public class OperationLogDO extends BaseDO {
    private static final long serialVersionUID = 2296107447523428408L;
    /**
     * 操作用户
     */
    private String username;
    /**
     * 模块id
     */
    private Long moduleId;
    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 操作内容
     */
    private String operation;
    /**
     * 耗时
     */
    private Long costTimes;
    /**
     * 操作方法
     */
    private String method;
    /**
     * 方法参数
     */
    private String params;
    /**
     * 操作者IP
     */
    private String ip;
    /**
     * 操作地点
     */
    private String location;
    /**
     * 访问来源
     */
    private String accessFrom;

}
