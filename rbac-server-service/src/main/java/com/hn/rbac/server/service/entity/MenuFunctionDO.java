package com.hn.rbac.server.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("menu_function")
public class MenuFunctionDO extends BaseDO {
    private static final long serialVersionUID = -2469959053307631411L;
    private Long menuId;
    private Long functionId;
}
