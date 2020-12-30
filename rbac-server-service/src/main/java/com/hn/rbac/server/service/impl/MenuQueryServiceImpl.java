package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.service.MenuQueryService;
import com.hn.rbac.server.service.dao.MenuMapper;
import com.hn.rbac.server.service.entity.MenuDO;
import com.hn.rbac.server.service.entity.converter.MenuConverter;
import com.hn.rbac.server.service.impl.wrapper.BaseQueryWrapperBuilder;
import com.hn.rbac.server.service.utils.SortUtil;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.request.enums.MenuTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.share.request.query.MenuQuery;
import com.hn.rbac.server.share.request.query.SortModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuQueryServiceImpl extends ServiceImpl<MenuMapper, MenuDO> implements MenuQueryService {

    @Override
    public Long findTotal(MenuQuery menuQuery, List<SortModel> sortModels) {
        try {
            Page<MenuDO> page = new Page<>(menuQuery.getPageNo(), menuQuery.getPageSize());
            QueryWrapper<MenuDO> queryWrapper = buildQueryWrapper(menuQuery, sortModels);
            return this.page(page, queryWrapper).getTotal();
        } catch (Exception e) {
            log.error("获取菜单/按钮信息总记录数失败", e);
            return null;
        }
    }

    @Override
    public List<Menu> findMenus(MenuQuery menuQuery, List<SortModel> sortModels) {
        try {
            Page<MenuDO> page = new Page<>(menuQuery.getPageNo(), menuQuery.getPageSize());
            QueryWrapper<MenuDO> queryWrapper = buildQueryWrapper(menuQuery, sortModels);
            return MenuConverter.INSTANCE.from(this.page(page, queryWrapper).getRecords());
        } catch (Exception e) {
            log.error("获取菜单/按钮信息失败", e);
            return null;
        }
    }

    @Override
    public PageInfo<Menu> findMenuPage(MenuQuery menuQuery, List<SortModel> sortModels) {
        PageInfo<Menu> menuPageInfo = new PageInfo<>(menuQuery.getPageNo(), menuQuery.getPageSize());
        Long totalCount = this.findTotal(menuQuery, sortModels);
        if (totalCount == 0L) {
            return menuPageInfo;
        }
        menuPageInfo.setTotalCount(totalCount);
        menuPageInfo.setData(this.findMenus(menuQuery, sortModels));
        return menuPageInfo;
    }

    private QueryWrapper<MenuDO> buildQueryWrapper(MenuQuery menuQuery, List<SortModel> sortModels) {

        QueryWrapper<MenuDO> queryWrapper = new QueryWrapper<>();
        // build
        BaseQueryWrapperBuilder.build(queryWrapper, menuQuery);

        if (!CollectionUtils.isEmpty(menuQuery.getBatchIds())) {
            queryWrapper.lambda().in(MenuDO::getId, menuQuery.getBatchIds());
        }
        if (StringUtils.isNotEmpty(menuQuery.getMenuNameKeyWords())) {
            queryWrapper.lambda().like(MenuDO::getMenuName, menuQuery.getMenuNameKeyWords());
        }
        if (menuQuery.getParentId() != null) {
            queryWrapper.lambda().eq(MenuDO::getParentId, menuQuery.getParentId());
        }
        if (StringUtils.isNotBlank(menuQuery.getMenuName())) {
            queryWrapper.lambda().eq(MenuDO::getMenuName, menuQuery.getMenuName());
        }
        if (StringUtils.isNotBlank(menuQuery.getType())) {
            queryWrapper.lambda().eq(MenuDO::getType, menuQuery.getType());
        }
        if (menuQuery.getStatus() != null) {
            queryWrapper.lambda().eq(MenuDO::getStatus, menuQuery.getStatus());
        }

        SortUtil.handleWrapperSort(queryWrapper, sortModels);

        return queryWrapper;
    }

    @Override
    public List<Menu> findMenusByBatchIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }
        return MenuConverter.INSTANCE.from(baseMapper.selectBatchIds(idList));
    }

    @Override
    public List<Menu> findValidMenusByBatchIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }
        return MenuConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<MenuDO>()
                                                                         .in(MenuDO::getId, idList)
                                                                         .eq(MenuDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public List<Long> findValidMenuIdsByBatchIds(List<Long> idList) {
        List<Menu> validMenus = this.findValidMenusByBatchIds(idList);
        if (!CollectionUtils.isEmpty(validMenus)) {
            return validMenus.stream().map(Menu::getId).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Long findValidAppIdByMenuId(Long menuId) {
        if (menuId == null) {
            return null;
        }
        Menu menu = this.findValidMenuById(menuId);
        if (menu == null) {
            return null;
        }
        if (menu.getParentId().equals(AdminConstant.MENU_ROOT_ID)) {
            return menuId;
        }
        // function类型的记录不维护parent_ids字段，特殊处理，找到它的parent menu
        if (menu.getType().equals(MenuTypeEnum.FUNCTION.getValue())) {
            menu = this.findValidMenuById(menu.getParentId());
        }
        String parentIds = menu.getParentIds();
        // parent_ids字段为空，且menu_type不是function的情况，是APP
        if (StringUtils.isEmpty(parentIds)) {
            return menu.getId();
        }
        return Long.parseLong(parentIds.split(StringPool.COMMA)[0]);
    }

    @Override
    public List<Menu> findAllValidApps() {
        return MenuConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<MenuDO>()
                                                                         .eq(MenuDO::getParentId, AdminConstant.MENU_ROOT_ID)
                                                                         .eq(MenuDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public Menu findValidMenuById(Long menuId) {
        if (menuId == null) {
            return null;
        }
        return MenuConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<MenuDO>()
                                                                        .eq(MenuDO::getId, menuId)
                                                                        .eq(MenuDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public Menu findByName(String menuName) {
        if (StringUtils.isEmpty(menuName)) {
            return null;
        }
        return MenuConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<MenuDO>().eq(MenuDO::getMenuName, menuName)));
    }

    @Override
    public Menu findByUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        return MenuConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<MenuDO>().eq(MenuDO::getUrl, url)));
    }

    @Override
    public Menu findByParentIdAndName(Long parentId, String menuName) {
        if (StringUtils.isEmpty(menuName)) {
            return null;
        }
        if (parentId == null) {
            return this.findByName(menuName);
        }
        return MenuConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<MenuDO>()
                                                                        .eq(MenuDO::getParentId, parentId)
                                                                        .eq(MenuDO::getMenuName, menuName)));
    }

    @Override
    public List<Menu> findAllValidMenusList() {
        Optional<List<MenuDO>> menuListOptional = Optional.ofNullable(
                baseMapper.selectList(new LambdaQueryWrapper<MenuDO>().eq(MenuDO::getStatus, StatusEnum.VALID.getValue())));
        if (menuListOptional.isPresent()) {
            return MenuConverter.INSTANCE.from(menuListOptional.get());
        }
        return null;
    }

    @Override
    public List<Long> findAllSubMenuIdList(Long menuId) {
        if (menuId == null) {
            return null;
        }

        QueryWrapper<MenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("parent_ids", menuId).or()
                                           .likeRight("parent_ids", menuId+StringPool.COMMA).or()
                                           .like("parent_ids",StringPool.COMMA+menuId+StringPool.COMMA).or()
                                           .likeLeft("parent_ids", StringPool.COMMA+menuId));

        Optional<List<MenuDO>> menuListOptional = Optional.ofNullable(baseMapper.selectList(queryWrapper));

        if (menuListOptional.isPresent()) {
            List<Long> menuIdList = menuListOptional.get().stream().map(MenuDO::getId).distinct().collect(Collectors.toList());
            return menuIdList;
        }

        return null;
    }

}
