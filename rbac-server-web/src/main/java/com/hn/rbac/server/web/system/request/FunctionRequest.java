package com.hn.rbac.server.web.system.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class FunctionRequest implements Serializable {
    private static final long serialVersionUID = -1322189814833741650L;
    /**
     * 功能ID
     */
    private Long id;
    /**
     * 应用ID
     */
    private Long appId;
    /**
     * 功能权限码
     */
    private String functionCode;
    /**
     * 功能名称
     */
    private String functionName;
    /**
     * 接口id
     */
    private String interfaceId;
    /**
     * 权限码创建人
     */
    private String owner;
    /**
     * 功能权限说明
     */
    private String remark;
}
