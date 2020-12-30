package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.service.entity.MenuDO;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.request.query.MenuQuery;
import com.hn.rbac.server.share.request.query.SortModel;

import java.util.List;

public interface MenuQueryService extends IService<MenuDO> {

    Long findTotal(MenuQuery menuQuery, List<SortModel> sortModels);

    List<Menu> findMenus(MenuQuery menuQuery, List<SortModel> sortModels);

    PageInfo<Menu> findMenuPage(MenuQuery menuQuery, List<SortModel> sortModels);

    List<Menu> findMenusByBatchIds(List<Long> idList);

    List<Menu> findValidMenusByBatchIds(List<Long> idList);

    List<Long> findValidMenuIdsByBatchIds(List<Long> idList);

    /**
     * 查询指定菜单所属应用的id
     * @return
     */
    Long findValidAppIdByMenuId(Long menuId);

    List<Menu> findAllValidApps();

    Menu findValidMenuById(Long menuId);

    Menu findByName(String menuName);

    Menu findByUrl(String url);

    Menu findByParentIdAndName(Long parentId, String menuName);

    List<Menu> findAllValidMenusList();

    /**
     * 查询所有子菜单ID
     * @param menuId
     * @return
     */
    List<Long> findAllSubMenuIdList(Long menuId);

}
