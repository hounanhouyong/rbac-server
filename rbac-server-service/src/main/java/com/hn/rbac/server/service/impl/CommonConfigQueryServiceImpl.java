package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.CommonConfigQueryService;
import com.hn.rbac.server.service.dao.CommonConfigMapper;
import com.hn.rbac.server.service.entity.CommonConfigDO;
import com.hn.rbac.server.service.entity.converter.CommonConfigConverter;
import com.hn.rbac.server.share.model.CommonConfig;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.share.request.query.CommonConfigQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommonConfigQueryServiceImpl extends ServiceImpl<CommonConfigMapper, CommonConfigDO> implements CommonConfigQueryService {
    @Override
    public List<CommonConfig> findConfig(CommonConfigQuery query) {
        return CommonConfigConverter.INSTANCE.from(
                baseMapper.selectList(new LambdaQueryWrapper<CommonConfigDO>()
                                              .eq(CommonConfigDO::getGroupId, query.getGroupId())
                                              .eq(CommonConfigDO::getDataId, query.getDataId())
                                              .eq(CommonConfigDO::getConfigKey, query.getConfigKey())
                                              .eq(CommonConfigDO::getStatus, StatusEnum.VALID.getValue())));
    }
}
