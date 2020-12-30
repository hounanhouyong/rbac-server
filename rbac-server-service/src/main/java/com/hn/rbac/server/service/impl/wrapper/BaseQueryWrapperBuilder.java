package com.hn.rbac.server.service.impl.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hn.rbac.server.service.entity.BaseDO;
import com.hn.rbac.server.share.request.query.BaseQuery;
import org.springframework.util.StringUtils;

public class BaseQueryWrapperBuilder {

    public static void build(QueryWrapper<? extends BaseDO> queryWrapper, BaseQuery baseQuery) {
        if (baseQuery.getId() != null) {
            queryWrapper.lambda().eq(BaseDO::getId, baseQuery.getId());
        }
        if (!StringUtils.isEmpty(baseQuery.getGeGmtCreate())) {
            queryWrapper.lambda().ge(BaseDO::getGmtCreate, baseQuery.getGeGmtCreate());
        }
        if (!StringUtils.isEmpty(baseQuery.getGtGmtCreate())) {
            queryWrapper.lambda().gt(BaseDO::getGmtCreate, baseQuery.getGtGmtCreate());
        }
        if (!StringUtils.isEmpty(baseQuery.getLeGmtCreate())) {
            queryWrapper.lambda().le(BaseDO::getGmtCreate, baseQuery.getLeGmtCreate());
        }
        if (!StringUtils.isEmpty(baseQuery.getLtGmtCreate())) {
            queryWrapper.lambda().lt(BaseDO::getGmtCreate, baseQuery.getLtGmtCreate());
        }
        if (!StringUtils.isEmpty(baseQuery.getGeGmtModified())) {
            queryWrapper.lambda().ge(BaseDO::getGmtModified, baseQuery.getGeGmtModified());
        }
        if (!StringUtils.isEmpty(baseQuery.getGtGmtModified())) {
            queryWrapper.lambda().gt(BaseDO::getGmtModified, baseQuery.getGtGmtModified());
        }
        if (!StringUtils.isEmpty(baseQuery.getLeGmtModified())) {
            queryWrapper.lambda().le(BaseDO::getGmtModified, baseQuery.getLeGmtModified());
        }
        if (!StringUtils.isEmpty(baseQuery.getLtGmtModified())) {
            queryWrapper.lambda().lt(BaseDO::getGmtModified, baseQuery.getLtGmtModified());
        }
    }

}
