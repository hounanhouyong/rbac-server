package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.MenuDO;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.request.enums.StatusEnum;

import java.util.List;

public interface MenuCommandService extends IService<MenuDO> {

    Long createMenu(Menu menu);

    void updateMenu(Menu menu, String functionIds) throws Exception;

    void updateMenuStatus(Long menuId, StatusEnum statusEnum) throws Exception;

    void updateMenusStatus(List<Long> menuIds, StatusEnum statusEnum) throws Exception;

    /**
     * 菜单排序功能
     * @param menuIdArray
     * @param targetVal         需要被替换的value
     * @param replaceValue      替换后的value
     * @throws Exception
     */
    void updateMenuOrderNum(String[] menuIdArray, String targetVal, String replaceValue) throws Exception;

    void bindFunctions(Long menuId, String functionIds) throws Exception;
}
