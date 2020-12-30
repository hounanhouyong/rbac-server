package com.hn.rbac.server.web.system.vo;

import com.hn.rbac.server.share.model.Function;
import lombok.Data;

@Data
public class FunctionVO extends Function {
    private static final long serialVersionUID = 4393992820420126704L;
    /**
     * 系统名称
     */
    private String appName;
}
