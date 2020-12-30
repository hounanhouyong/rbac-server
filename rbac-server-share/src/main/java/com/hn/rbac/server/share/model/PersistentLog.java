package com.hn.rbac.server.share.model;

import com.hn.rbac.server.share.request.enums.PersistentObjTypeEnum;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PersistentLog extends BaseModel {
    private static final long serialVersionUID = 2362615787218153192L;
    private String username;
    /**
     * @see PersistentObjTypeEnum
     */
    private String objType;
    private Long objId;
    private String currentValue;
    private String modifiedValue;
}
