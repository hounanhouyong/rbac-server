package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.MenuFunctionDO;
import com.hn.rbac.server.share.model.MenuFunction;

import java.util.List;

public interface MenuFunctionQueryService extends IService<MenuFunctionDO> {

    List<Long> findFunctionIdsByMenuId(Long menuId);

    List<Long> findFunctionIdsByBatchMenuIds(List<Long> menuIds);

    List<MenuFunction> findByBatchMenuIds(List<Long> menuIds);

}
