package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.service.*;
import com.hn.rbac.server.service.dao.FunctionMapper;
import com.hn.rbac.server.service.entity.FunctionDO;
import com.hn.rbac.server.service.entity.converter.FunctionConverter;
import com.hn.rbac.server.service.impl.wrapper.BaseQueryWrapperBuilder;
import com.hn.rbac.server.service.utils.SortUtil;
import com.hn.rbac.server.share.model.Function;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.model.User;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.share.request.query.FunctionQuery;
import com.hn.rbac.server.share.request.query.SortModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FunctionQueryServiceImpl extends ServiceImpl<FunctionMapper, FunctionDO> implements FunctionQueryService {

    @Resource
    private UserQueryService userQueryService;
    @Resource
    private UserRoleQueryService userRoleQueryService;
    @Resource
    private RoleQueryService roleQueryService;
    @Resource
    private RoleMenuQueryService roleMenuQueryService;
    @Resource
    private MenuQueryService menuQueryService;
    @Resource
    private MenuFunctionQueryService menuFunctionQueryService;

    @Override
    public Long findTotal(FunctionQuery functionQuery, List<SortModel> sortModels) {
        try {
            Page<FunctionDO> page = new Page<>(functionQuery.getPageNo(), functionQuery.getPageSize());
            QueryWrapper<FunctionDO> queryWrapper = buildQueryWrapper(functionQuery, sortModels);
            return this.page(page, queryWrapper).getTotal();
        } catch (Exception e) {
            log.error("获取接口权限信息总记录数失败", e);
            return null;
        }
    }

    @Override
    public List<Function> findFunctions(FunctionQuery functionQuery, List<SortModel> sortModels) {
        try {
            Page<FunctionDO> page = new Page<>(functionQuery.getPageNo(), functionQuery.getPageSize());
            QueryWrapper<FunctionDO> queryWrapper = buildQueryWrapper(functionQuery, sortModels);
            return FunctionConverter.INSTANCE.from(this.page(page, queryWrapper).getRecords());
        } catch (Exception e) {
            log.error("获取接口权限信息失败", e);
            return null;
        }
    }

    @Override
    public PageInfo<Function> findFunctionPage(FunctionQuery functionQuery, List<SortModel> sortModels) {
        PageInfo<Function> functionPageInfo = new PageInfo<>(functionQuery.getPageNo(), functionQuery.getPageSize());
        Long totalCount = this.findTotal(functionQuery, sortModels);
        if (totalCount == 0L) {
            return functionPageInfo;
        }
        functionPageInfo.setTotalCount(totalCount);
        functionPageInfo.setData(this.findFunctions(functionQuery, sortModels));
        return functionPageInfo;
    }

    @Override
    public Function findValidFunctionById(Long functionId) {
        if (functionId == null) {
            return null;
        }
        return FunctionConverter.INSTANCE.from(this.baseMapper.selectOne(new LambdaQueryWrapper<FunctionDO>()
                                                                                 .eq(FunctionDO::getId, functionId)
                                                                                 .eq(FunctionDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public List<Function> findValidFunctionByBatchIds(List<Long> functionIdList) {
        if (CollectionUtils.isEmpty(functionIdList)) {
            return null;
        }
        return FunctionConverter.INSTANCE.from(this.baseMapper.selectList(new LambdaQueryWrapper<FunctionDO>()
                                                                                  .in(FunctionDO::getId, functionIdList)
                                                                                  .eq(FunctionDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public List<String> findValidFunctionCodeByBatchIds(List<Long> functionIdList) {
        if (CollectionUtils.isEmpty(functionIdList)) {
            return null;
        }
        List<Function> functionList = this.findValidFunctionByBatchIds(functionIdList);
        if (!CollectionUtils.isEmpty(functionList)) {
            return functionList.stream().map(Function::getFunctionCode).distinct().collect(Collectors.toList());
        }
        return null;

    }

    @Override
    public Function findValidFunctionByAppIdAndName(Long appId, String functionName) {
        if (appId == null) {
            appId = 0L;
        }
        if (StringUtils.isEmpty(functionName)) {
            return null;
        }
        return FunctionConverter.INSTANCE.from(this.baseMapper.selectOne(new LambdaQueryWrapper<FunctionDO>()
                                                                                 .eq(FunctionDO::getAppId, appId)
                                                                                 .eq(FunctionDO::getFunctionName, functionName)
                                                                                 .eq(FunctionDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public Function findValidFunctionByAppIdAndCode(Long appId, String functionCode) {
        if (appId == null) {
            appId = 0L;
        }
        if (StringUtils.isEmpty(functionCode)) {
            return null;
        }
        return FunctionConverter.INSTANCE.from(this.baseMapper.selectOne(new LambdaQueryWrapper<FunctionDO>()
                                                                                 .eq(FunctionDO::getAppId, appId)
                                                                                 .eq(FunctionDO::getFunctionCode, functionCode)
                                                                                 .eq(FunctionDO::getStatus, StatusEnum.VALID.getValue())));
    }

    @Override
    public Function findValidFunctionByCode(String functionCode) {
        if (StringUtils.isEmpty(functionCode)) {
            return null;
        }
        return FunctionConverter.INSTANCE.from(this.baseMapper.selectOne(new LambdaQueryWrapper<FunctionDO>()
                                                                                 .eq(FunctionDO::getFunctionCode, functionCode)));
    }

    @Override
    public List<String> findFunctionCodeList(String username) {
        // username -> user
        User user = userQueryService.findValidUserByName(username);
        if (user == null) {
            return null;
        }
        // user <-> role
        List<Long> roleIds = userRoleQueryService.findRoleIdsByUserId(user.getId());
        // superAdmin判断
        if (roleQueryService.hasValidSuperAdminRole(roleIds)) {
            // is superAdmin, has all permissions
            return this.findAllValidFunctionCodeList();
        } else {
            // role <-> menu
            List<Long> menuIds = roleMenuQueryService.findMenuIdsByBatchRoleIds(roleIds);
            List<Long> validMenuIds = menuQueryService.findValidMenuIdsByBatchIds(menuIds);
            // menu <-> function
            List<Long> functionIds = menuFunctionQueryService.findFunctionIdsByBatchMenuIds(validMenuIds);
            List<Long> commonFunctionIds = menuFunctionQueryService.findFunctionIdsByMenuId(AdminConstant.MENU_ROOT_ID);

            List<String> validFunctionCodeList = this.findValidFunctionCodeByBatchIds(functionIds);
            List<String> commonValidFunctionCodeList = this.findValidFunctionCodeByBatchIds(commonFunctionIds);

            // permissions
            if (CollectionUtils.isNotEmpty(validFunctionCodeList) && CollectionUtils.isNotEmpty(commonValidFunctionCodeList)) {
                // 并集
                return Stream.of(validFunctionCodeList, commonValidFunctionCodeList).flatMap(Collection::stream).distinct().collect(Collectors.toList());
            } else if (CollectionUtils.isNotEmpty(validFunctionCodeList)) {
                return validFunctionCodeList;
            } else {
                return commonValidFunctionCodeList;
            }
        }
    }

    @Override
    public List<String> findAllValidFunctionCodeList() {
        Optional<List<FunctionDO>> functionDOListOptional = Optional.ofNullable(
                this.baseMapper.selectList(new LambdaQueryWrapper<FunctionDO>().eq(FunctionDO::getStatus, StatusEnum.VALID.getValue())));
        if (functionDOListOptional.isPresent()) {
            return functionDOListOptional.get().stream().map(FunctionDO::getFunctionCode).distinct().collect(Collectors.toList());
        }
        return null;
    }

    private QueryWrapper<FunctionDO> buildQueryWrapper(FunctionQuery functionQuery, List<SortModel> sortModels) {

        QueryWrapper<FunctionDO> queryWrapper = new QueryWrapper<>();
        // build
        BaseQueryWrapperBuilder.build(queryWrapper, functionQuery);

        if (StringUtils.isNotEmpty(functionQuery.getAppName())) {
            Menu menu = menuQueryService.findByName(functionQuery.getAppName());
            if (menu != null) {
                queryWrapper.lambda().eq(FunctionDO::getAppId, menu.getId());
            }
        }
        if (StringUtils.isNotEmpty(functionQuery.getFunctionNameKeyWords())) {
            queryWrapper.lambda().like(FunctionDO::getFunctionName, functionQuery.getFunctionNameKeyWords());
        }
        if (StringUtils.isNotEmpty(functionQuery.getFunctionCodeKeyWords())) {
            queryWrapper.lambda().like(FunctionDO::getFunctionCode, functionQuery.getFunctionCode());
        }
        if (StringUtils.isNotBlank(functionQuery.getOwner())) {
            queryWrapper.lambda().eq(FunctionDO::getOwner, functionQuery.getOwner());
        }
        if (functionQuery.getAppId() != null) {
            queryWrapper.lambda().eq(FunctionDO::getAppId, functionQuery.getAppId());
        }
        if (functionQuery.getStatus() != null) {
            queryWrapper.lambda().eq(FunctionDO::getStatus, functionQuery.getStatus());
        }

        SortUtil.handleWrapperSort(queryWrapper, sortModels);

        return queryWrapper;
    }



}
