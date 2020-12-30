package com.hn.rbac.server.share.model;

import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
public class Function extends BaseModel {
    private static final long serialVersionUID = -5966381039716316136L;
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
     * @see StatusEnum
     */
    private Integer status;
    /**
     * 功能权限说明
     */
    private String remark;
}
