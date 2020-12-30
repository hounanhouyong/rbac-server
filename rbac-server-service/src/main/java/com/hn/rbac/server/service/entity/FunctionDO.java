package com.hn.rbac.server.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
@TableName("function")
public class FunctionDO extends BaseDO {
    private static final long serialVersionUID = -8129676096706427406L;
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
