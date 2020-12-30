package com.hn.rbac.server.share.model;

import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
public class CommonConfig extends BaseModel {
    private static final long serialVersionUID = -2439855380691613381L;
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
