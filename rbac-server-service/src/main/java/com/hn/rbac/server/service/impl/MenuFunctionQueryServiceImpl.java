package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.MenuFunctionQueryService;
import com.hn.rbac.server.service.dao.MenuFunctionMapper;
import com.hn.rbac.server.service.entity.MenuFunctionDO;
import com.hn.rbac.server.service.entity.converter.MenuFunctionConverter;
import com.hn.rbac.server.share.model.MenuFunction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuFunctionQueryServiceImpl extends ServiceImpl<MenuFunctionMapper, MenuFunctionDO> implements MenuFunctionQueryService {
    @Override
    public List<Long> findFunctionIdsByMenuId(Long menuId) {
        if (menuId == null) {
            return null;
        }
        Optional<List<MenuFunctionDO>> optional = Optional.ofNullable(
                baseMapper.selectList(new LambdaQueryWrapper<MenuFunctionDO>().eq(MenuFunctionDO::getMenuId, menuId)));
        if (optional.isPresent()) {
            return optional.get().stream().map(MenuFunctionDO::getFunctionId).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Long> findFunctionIdsByBatchMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return null;
        }
        Optional<List<MenuFunction>> optional = Optional.ofNullable(this.findByBatchMenuIds(menuIds));
        if (optional.isPresent()) {
            return optional.get().stream().map(MenuFunction::getFunctionId).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<MenuFunction> findByBatchMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return null;
        }
        return MenuFunctionConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<MenuFunctionDO>()
                                                                                 .in(MenuFunctionDO::getMenuId, menuIds)));
    }
}
