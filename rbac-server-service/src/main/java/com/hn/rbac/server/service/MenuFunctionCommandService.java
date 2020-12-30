package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.MenuFunctionDO;

public interface MenuFunctionCommandService extends IService<MenuFunctionDO> {

    void saveMenuFunctions(Long menuId, String[] functionIds);

    void deleteMenuFunctionsByMenuId(Long... menuIds);
}
