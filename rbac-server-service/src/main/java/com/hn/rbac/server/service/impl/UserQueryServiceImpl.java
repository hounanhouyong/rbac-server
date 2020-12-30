package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.service.UserQueryService;
import com.hn.rbac.server.service.UserRoleQueryService;
import com.hn.rbac.server.service.dao.RoleMapper;
import com.hn.rbac.server.service.dao.UserMapper;
import com.hn.rbac.server.service.entity.RoleDO;
import com.hn.rbac.server.service.entity.UserDO;
import com.hn.rbac.server.service.entity.converter.UserConverter;
import com.hn.rbac.server.service.impl.wrapper.BaseQueryWrapperBuilder;
import com.hn.rbac.server.service.utils.SortUtil;
import com.hn.rbac.server.share.model.User;
import com.hn.rbac.server.share.request.enums.OrderByEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.share.request.query.SortModel;
import com.hn.rbac.server.share.request.query.UserQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserQueryServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserQueryService {

    @Resource
    private UserRoleQueryService userRoleQueryService;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public Long findTotal(UserQuery userQuery, List<SortModel> sortModels) {
        try {
            Page<UserDO> page = new Page<>(userQuery.getPageNo(), userQuery.getPageSize());
            QueryWrapper<UserDO> queryWrapper = buildQueryWrapper(userQuery, sortModels);
            return this.page(page, queryWrapper).getTotal();
        } catch (Exception e) {
            log.error("获取用户信息总记录数失败", e);
            return null;
        }
    }

    @Override
    public List<User> findUsers(UserQuery userQuery, List<SortModel> sortModels) {
        try {
            Page<UserDO> page = new Page<>(userQuery.getPageNo(), userQuery.getPageSize());
            QueryWrapper<UserDO> queryWrapper = buildQueryWrapper(userQuery, sortModels);
            return UserConverter.INSTANCE.from(this.page(page, queryWrapper).getRecords());
        } catch (Exception e) {
            log.error("获取用户息总记录数失败", e);
            return null;
        }
    }

    @Override
    public PageInfo<User> findUserPage(UserQuery userQuery, List<SortModel> sortModels) {
        PageInfo<User> userPageInfo = new PageInfo<>(userQuery.getPageNo(), userQuery.getPageSize());
        Long totalCount = this.findTotal(userQuery, sortModels);
        if (totalCount == 0L) {
            return userPageInfo;
        }
        userPageInfo.setTotalCount(totalCount);
        userPageInfo.setData(this.findUsers(userQuery, sortModels));
        return userPageInfo;
    }

    private QueryWrapper<UserDO> buildQueryWrapper(UserQuery userQuery, List<SortModel> sortModels) {

        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        // build
        BaseQueryWrapperBuilder.build(queryWrapper, userQuery);

        if (StringUtils.isNotEmpty(userQuery.getDisplayNameKeyWords())) {
            queryWrapper.lambda().like(UserDO::getDisplayName, userQuery.getDisplayNameKeyWords());
        }
        if (StringUtils.isNotEmpty(userQuery.getUsernameKeyWords())) {
            queryWrapper.lambda().like(UserDO::getUsername, userQuery.getUsernameKeyWords());
        }
        if (StringUtils.isNotEmpty(userQuery.getRoleNameKeyWords())) {
            List<RoleDO> roleDOS =
                    roleMapper.selectList(new LambdaQueryWrapper<RoleDO>()
                                                  .like(RoleDO::getRoleName, userQuery.getRoleNameKeyWords())
                                                  .eq(RoleDO::getStatus, StatusEnum.VALID.getValue()));
            if (!CollectionUtils.isEmpty(roleDOS)) {
                // role <-> user
                List<Long> userIds = userRoleQueryService.findUserIdsByBatchRoleIds(roleDOS.stream().map(RoleDO::getId).collect(Collectors.toList()));
                if (!CollectionUtils.isEmpty(userIds)) {
                    queryWrapper.lambda().in(UserDO::getId, userIds);
                }
            } else {
                queryWrapper.lambda().eq(UserDO::getId, 0);
            }
        }
        if (StringUtils.isNotBlank(userQuery.getUsername())) {
            queryWrapper.lambda().eq(UserDO::getUsername, userQuery.getUsername());
        }
        if (StringUtils.isNotBlank(userQuery.getDisplayName())) {
            queryWrapper.lambda().eq(UserDO::getDisplayName, userQuery.getDisplayName());
        }
        if (userQuery.getStatus() != null) {
            queryWrapper.lambda().eq(UserDO::getStatus, userQuery.getStatus());
        }

        SortUtil.handleWrapperSort(queryWrapper, sortModels);

        return queryWrapper;
    }

    @Override
    public User findByName(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        return UserConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<UserDO>().eq(UserDO::getUsername, username)));
    }

    @Override
    public User findValidUserByName(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        return UserConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<UserDO>()
                                                                        .eq(UserDO::getUsername, username)
                                                                        .eq(UserDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public User findById(Long id) {
        if (id == null) {
            return null;
        }
        return UserConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<UserDO>()
                                                                        .eq(UserDO::getId, id)));
    }

    @Override
    public User findValidUserById(Long id) {
        if (id == null) {
            return null;
        }
        return UserConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<UserDO>()
                                                                        .eq(UserDO::getId, id)
                                                                        .eq(UserDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public User findValidUserByEmpId(Long empId) {
        if (empId == null) {
            return null;
        }
        return UserConverter.INSTANCE.from(baseMapper.selectOne(new LambdaQueryWrapper<UserDO>()
                                                                        .eq(UserDO::getEmpId, empId)
                                                                        .eq(UserDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public List<User> findByBatchIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }
        return UserConverter.INSTANCE.from(baseMapper.selectBatchIds(idList));
    }

    @Override
    public List<User> findValidUsersByBatchIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }
        return UserConverter.INSTANCE.from(baseMapper.selectList(new LambdaQueryWrapper<UserDO>()
                                                                         .in(UserDO::getId, idList)
                                                                         .eq(UserDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public List<String> findAllUsername() {
        List<String> usernameList = new ArrayList<>();

        int pageNo = 1;
        int pageSize = 2000;
        int rows = pageSize;

        UserQuery userQuery = new UserQuery();
        userQuery.setPageSize(pageSize);
        SortModel sortModel = new SortModel("id", OrderByEnum.ASC.getValue());
        List<SortModel> sortModels = new ArrayList<>();
        sortModels.add(sortModel);

        while (rows == pageSize) {
            userQuery.setPageNo(pageNo);
            List<User> userList = this.findUsers(userQuery, sortModels);
            if (!CollectionUtils.isEmpty(userList)) {
                rows = userList.size();
                for (User user : userList) {
                    usernameList.add(user.getUsername());
                }
                pageNo = pageNo + 1;
            } else {
                rows = 0;
            }
        }

        return usernameList;
    }

}
