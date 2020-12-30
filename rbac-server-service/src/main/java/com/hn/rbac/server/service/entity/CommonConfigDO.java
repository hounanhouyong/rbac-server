package com.hn.rbac.server.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
@TableName("common_config")
public class CommonConfigDO extends BaseDO {
    private static final long serialVersionUID = -672790320271552532L;
    private String groupId;
    private String dataId;
    private String configKey;
    private String configValue;
    /**
     * @see StatusEnum
     */
    private Integer status;
    private String remark;
}
