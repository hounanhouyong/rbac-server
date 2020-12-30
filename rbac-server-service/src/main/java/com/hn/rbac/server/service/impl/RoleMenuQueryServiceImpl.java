package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.RoleMenuQueryService;
import com.hn.rbac.server.service.dao.RoleMenuMapper;
import com.hn.rbac.server.service.entity.RoleMenuDO;
import com.hn.rbac.server.service.entity.converter.RoleMenuConverter;
import com.hn.rbac.server.share.model.RoleMenu;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleMenuQueryServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuDO> implements RoleMenuQueryService {

    @Override
    public List<Long> findMenuIdsByRoleId(Long roleId) {
        if (roleId == null) {
            return null;
        }
        Optional<List<RoleMenu>> optional = Optional.ofNullable(this.findByRoleId(roleId));
        if (optional.isPresent()) {
            return optional.get().stream().map(RoleMenu::getMenuId).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Long> findMenuIdsByBatchRoleIds(List<Long> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return null;
        }
        Optional<List<RoleMenu>> optional = Optional.ofNullable(this.findByBatchRoleIds(roleIdList));
        if (optional.isPresent()) {
            return optional.get().stream().map(RoleMenu::getMenuId).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Long> findRoleIdsByMenuId(Long menuId) {
        if (menuId == null) {
            return null;
        }
        Optional<List<RoleMenuDO>> optional = Optional.ofNullable(
                baseMapper.selectList(new LambdaQueryWrapper<RoleMenuDO>().eq(RoleMenuDO::getMenuId, menuId)));
        if (optional.isPresent()) {
            optional.get().stream().map(RoleMenuDO::getRoleId).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Long> findRoleIdsByBatchMenuIds(List<Long> menuIdList) {
        if (CollectionUtils.isEmpty(menuIdList)) {
            return null;
        }
        Optional<List<RoleMenuDO>> optional = Optional.ofNullable(
                baseMapper.selectList(new LambdaQueryWrapper<RoleMenuDO>().in(RoleMenuDO::getMenuId, menuIdList)));
        if (optional.isPresent()) {
            return optional.get().stream().map(RoleMenuDO::getRoleId).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<RoleMenu> findByRoleId(Long roleId) {
        if (roleId == null) {
            return null;
        }
        return RoleMenuConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<RoleMenuDO>().eq(RoleMenuDO::getRoleId, roleId)));
    }

    @Override
    public List<RoleMenu> findByBatchRoleIds(List<Long> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return null;
        }
        return RoleMenuConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<RoleMenuDO>().in(RoleMenuDO::getRoleId, roleIdList)));
    }

}
