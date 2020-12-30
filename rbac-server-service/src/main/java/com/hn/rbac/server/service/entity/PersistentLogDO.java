package com.hn.rbac.server.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hn.rbac.server.share.request.enums.PersistentObjTypeEnum;
import lombok.Data;

@Data
@TableName("persistent_log")
public class PersistentLogDO extends BaseDO {
    private static final long serialVersionUID = -2883553010616790183L;
    private String username;
    /**
     * @see PersistentObjTypeEnum
     */
    private String objType;
    private Long objId;
    private String currentValue;
    private String modifiedValue;
}
