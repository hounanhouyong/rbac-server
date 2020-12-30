package com.hn.rbac.server.share.model;

import lombok.Data;

@Data
public class MenuFunction extends BaseModel {
    private static final long serialVersionUID = -1343571217901753605L;
    private Long menuId;
    private Long functionId;
}
