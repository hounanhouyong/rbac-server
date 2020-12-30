package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.service.OpLogQueryService;
import com.hn.rbac.server.service.dao.MenuMapper;
import com.hn.rbac.server.service.dao.OperationLogMapper;
import com.hn.rbac.server.service.entity.MenuDO;
import com.hn.rbac.server.service.entity.OperationLogDO;
import com.hn.rbac.server.service.entity.converter.OpLogConverter;
import com.hn.rbac.server.service.impl.wrapper.BaseQueryWrapperBuilder;
import com.hn.rbac.server.service.utils.SortUtil;
import com.hn.rbac.server.share.model.OperationLog;
import com.hn.rbac.server.share.request.query.OpLogQuery;
import com.hn.rbac.server.share.request.query.SortModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class OpLogQueryServiceImpl extends ServiceImpl<OperationLogMapper, OperationLogDO> implements OpLogQueryService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public Long findTotal(OpLogQuery opLogQuery, List<SortModel> sortModels) {
        try {
            Page<OperationLogDO> page = new Page<>(opLogQuery.getPageNo(), opLogQuery.getPageSize());
            QueryWrapper<OperationLogDO> queryWrapper = buildQueryWrapper(opLogQuery, sortModels);
            return this.page(page, queryWrapper).getTotal();
        } catch (Exception e) {
            log.error("获取系统日志总记录数失败", e);
            return null;
        }
    }


    @Override
    public List<OperationLog> findOpLogs(OpLogQuery opLogQuery, List<SortModel> sortModels) {
        try {
            Page<OperationLogDO> page = new Page<>(opLogQuery.getPageNo(), opLogQuery.getPageSize());
            QueryWrapper<OperationLogDO> queryWrapper = buildQueryWrapper(opLogQuery, sortModels);
            return OpLogConverter.INSTANCE.from(this.page(page, queryWrapper).getRecords());
        } catch (Exception e) {
            log.error("获取系统日志列表失败", e);
            return null;
        }
    }

    @Override
    public PageInfo<OperationLog> findOpLogPage(OpLogQuery opLogQuery, List<SortModel> sortModels) {
        PageInfo<OperationLog> opLogPageInfo = new PageInfo<>(opLogQuery.getPageNo(), opLogQuery.getPageSize());
        Long totalCount = this.findTotal(opLogQuery, sortModels);
        if (totalCount == 0L) {
            return opLogPageInfo;
        }
        opLogPageInfo.setTotalCount(totalCount);
        opLogPageInfo.setData(this.findOpLogs(opLogQuery, sortModels));
        return opLogPageInfo;
    }

    private QueryWrapper<OperationLogDO> buildQueryWrapper(OpLogQuery opLogQuery, List<SortModel> sortModels) {

        QueryWrapper<OperationLogDO> queryWrapper = new QueryWrapper<>();
        // build
        BaseQueryWrapperBuilder.build(queryWrapper, opLogQuery);

        if (StringUtils.isNotEmpty(opLogQuery.getUsernameKeyWords())) {
            queryWrapper.lambda().like(OperationLogDO::getUsername, opLogQuery.getUsernameKeyWords());
        }
        if (StringUtils.isNotEmpty(opLogQuery.getUsername())) {
            queryWrapper.lambda().eq(OperationLogDO::getUsername, opLogQuery.getUsername().toLowerCase());
        }
        if (opLogQuery.getModuleId() != null) {
            MenuDO menuDO = menuMapper.selectById(opLogQuery.getModuleId());
            if (menuDO != null) {
                String menuName = menuDO.getMenuName();
                queryWrapper.and(wrapper -> wrapper.eq("module_id", opLogQuery.getModuleId()).or().eq("module_name", menuName));
            } else {
                queryWrapper.lambda().eq(OperationLogDO::getModuleId, opLogQuery.getModuleId());
            }
        }

        SortUtil.handleWrapperSort(queryWrapper, sortModels);

        return queryWrapper;
    }

}
