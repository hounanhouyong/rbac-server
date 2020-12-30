package com.hn.rbac.server.share.request.query;

import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
public class FunctionQuery extends BaseQuery {
    private static final long serialVersionUID = -7498582104387574325L;
    /**
     * 应用ID
     */
    private Long appId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 功能权限码
     */
    private String functionCode;
    /**
     * 权限码创建人
     */
    private String owner;
    /**
     * @see StatusEnum
     */
    private Integer status;
    /**
     * 功能权限码模糊查询
     */
    private String functionCodeKeyWords;
    /**
     * 功能名称模糊查询
     */
    private String functionNameKeyWords;
}
