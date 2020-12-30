package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.UserRoleQueryService;
import com.hn.rbac.server.service.dao.UserRoleMapper;
import com.hn.rbac.server.service.entity.UserRoleDO;
import com.hn.rbac.server.service.entity.converter.UserRoleConverter;
import com.hn.rbac.server.share.model.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRoleQueryServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements UserRoleQueryService {

    @Override
    public List<Long> findUserIdsByRoleId(Long roleId) {
        if (roleId == null) {
            return null;
        }
        Optional<List<UserRoleDO>> optional = Optional.ofNullable(
                baseMapper.selectList(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getRoleId, roleId)));
        if (optional.isPresent()) {
            return optional.get().stream().map(userRoleDO -> userRoleDO.getUserId()).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Long> findUserIdsByBatchRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return null;
        }
        Optional<List<UserRoleDO>> optional = Optional.ofNullable(
                baseMapper.selectList(new LambdaQueryWrapper<UserRoleDO>().in(UserRoleDO::getRoleId, roleIds)));
        if (optional.isPresent()) {
            return optional.get().stream().map(userRoleDO -> userRoleDO.getUserId()).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Long> findRoleIdsByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        Optional<List<UserRoleDO>> optional = Optional.ofNullable(
                baseMapper.selectList(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getUserId, userId)));
        if (optional.isPresent()) {
            return optional.get().stream().map(userRoleDO -> userRoleDO.getRoleId()).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Long> findRoleIdsByBatchUserIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return null;
        }
        Optional<List<UserRoleDO>> optional = Optional.ofNullable(
                baseMapper.selectList(new LambdaQueryWrapper<UserRoleDO>().in(UserRoleDO::getUserId, userIds)));
        if (optional.isPresent()) {
            return optional.get().stream().map(userRoleDO -> userRoleDO.getRoleId()).distinct().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<UserRole> findByRoleId(Long roleId) {
        if (roleId == null) {
            return null;
        }
        return UserRoleConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getRoleId, roleId)));
    }

    @Override
    public List<UserRole> findByBatchRoleIds(List<Long> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return null;
        }
        return UserRoleConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<UserRoleDO>().in(UserRoleDO::getRoleId, roleIdList)));
    }

    @Override
    public List<UserRole> findByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return UserRoleConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getUserId, userId)));
    }

    @Override
    public List<UserRole> findByBatchUserId(List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return null;
        }
        return UserRoleConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<UserRoleDO>().in(UserRoleDO::getUserId, userIdList)));
    }
}
