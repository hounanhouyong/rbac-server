package com.hn.rbac.server.web.system.vo;

import com.hn.rbac.server.share.model.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuVO extends Menu {
    private static final long serialVersionUID = 5784214596990618443L;
    /**
     * 父菜单名称
     */
    private String parenMenuName;
    /**
     * 功能接口ID集合
     */
    private List<Long> functionIds = new ArrayList<>();
}
