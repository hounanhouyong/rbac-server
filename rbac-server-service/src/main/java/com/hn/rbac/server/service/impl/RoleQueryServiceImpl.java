package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.service.RoleQueryService;
import com.hn.rbac.server.service.UserQueryService;
import com.hn.rbac.server.service.UserRoleQueryService;
import com.hn.rbac.server.service.dao.RoleMapper;
import com.hn.rbac.server.service.entity.RoleDO;
import com.hn.rbac.server.service.entity.converter.RoleConverter;
import com.hn.rbac.server.service.impl.wrapper.BaseQueryWrapperBuilder;
import com.hn.rbac.server.service.utils.SortUtil;
import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.share.model.User;
import com.hn.rbac.server.share.request.enums.RoleTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.share.request.query.RoleQuery;
import com.hn.rbac.server.share.request.query.SortModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class RoleQueryServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements RoleQueryService {

    @Resource
    private UserRoleQueryService userRoleQueryService;
    @Resource
    private UserQueryService userQueryService;

    @Override
    public Long findTotal(RoleQuery roleQuery, List<SortModel> sortModels) {
        try {
            Page<RoleDO> page = new Page<>(roleQuery.getPageNo(), roleQuery.getPageSize());
            QueryWrapper<RoleDO> queryWrapper = buildQueryWrapper(roleQuery, sortModels);
            return this.page(page, queryWrapper).getTotal();
        } catch (Exception e) {
            log.error("获取角色信息总记录数失败", e);
            return null;
        }
    }

    @Override
    public List<Role> findRoles(RoleQuery roleQuery, List<SortModel> sortModels) {
        try {
            Page<RoleDO> page = new Page<>(roleQuery.getPageNo(), roleQuery.getPageSize());
            QueryWrapper<RoleDO> queryWrapper = buildQueryWrapper(roleQuery, sortModels);
            return RoleConverter.INSTANCE.from(this.page(page, queryWrapper).getRecords());
        } catch (Exception e) {
            log.error("获取角色信息列表总记录数失败", e);
            return null;
        }
    }

    @Override
    public PageInfo<Role> findRolePage(RoleQuery roleQuery, List<SortModel> sortModels) {
        PageInfo<Role> rolePageInfo = new PageInfo<>(roleQuery.getPageNo(), roleQuery.getPageSize());
        Long totalCount = this.findTotal(roleQuery, sortModels);
        if (totalCount == 0L) {
            return rolePageInfo;
        }
        rolePageInfo.setTotalCount(totalCount);
        rolePageInfo.setData(this.findRoles(roleQuery, sortModels));
        return rolePageInfo;
    }

    private QueryWrapper<RoleDO> buildQueryWrapper(RoleQuery roleQuery, List<SortModel> sortModels) {

        QueryWrapper<RoleDO> queryWrapper = new QueryWrapper<>();
        // build
        BaseQueryWrapperBuilder.build(queryWrapper, roleQuery);

        if (StringUtils.isNotEmpty(roleQuery.getRoleNameKeyWords())) {
            queryWrapper.lambda().like(RoleDO::getRoleName, roleQuery.getRoleNameKeyWords());
        }
        if (StringUtils.isNotBlank(roleQuery.getRoleName())) {
            queryWrapper.lambda().eq(RoleDO::getRoleName, roleQuery.getRoleName());
        }
        if (roleQuery.getStatus() != null) {
            queryWrapper.lambda().eq(RoleDO::getStatus, roleQuery.getStatus());
        }

        SortUtil.handleWrapperSort(queryWrapper, sortModels);

        return queryWrapper;
    }

    @Override
    public List<Role> findUserRole(String username) {
        User user = userQueryService.findValidUserByName(username);
        if (user == null) {
            return null;
        }
        List<Long> roleIdList = userRoleQueryService.findRoleIdsByUserId(user.getId());
        if (CollectionUtils.isEmpty(roleIdList)) {
            return null;
        }
        return this.findValidRolesByBatchIds(roleIdList);
    }

    @Override
    public Role findByName(String roleName) {
        return RoleConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<RoleDO>().eq(RoleDO::getRoleName, roleName)));
    }

    @Override
    public Role findValidRoleById(Long id) {
        if (id == null) {
            return null;
        }
        return RoleConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<RoleDO>()
                                                                        .eq(RoleDO::getId, id)
                                                                        .eq(RoleDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public List<Role> findValidRolesByBatchIds(List<Long> idList) {
        return this.findByBatchIdsAndStatus(idList, StatusEnum.VALID);
    }

    @Override
    public List<Role> findByBatchIdsAndStatus(List<Long> idList, StatusEnum statusEnum) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }
        return RoleConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<RoleDO>()
                                                                         .in(RoleDO::getId, idList)
                                                                         .eq(RoleDO::getStatus, statusEnum.getValue())));
    }

    @Override
    public boolean hasValidSuperAdminRole(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        List<RoleDO> roleList = baseMapper.selectList(new LambdaQueryWrapper<RoleDO>()
                                                              .in(RoleDO::getId, idList)
                                                              .eq(RoleDO::getStatus, StatusEnum.VALID.getValue())
                                                              .eq(RoleDO::getType, RoleTypeEnum.SUPER_ADMIN.getValue()));
        if (!CollectionUtils.isEmpty(roleList)) {
            return true;
        }

        return false;
    }

}
