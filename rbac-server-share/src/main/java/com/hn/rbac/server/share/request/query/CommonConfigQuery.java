package com.hn.rbac.server.share.request.query;

import lombok.Data;

@Data
public class CommonConfigQuery extends BaseQuery {
    private static final long serialVersionUID = -4706711484842024776L;
    private String groupId;
    private String dataId;
    private String configKey;
}
