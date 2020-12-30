package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.CommonConfigCommandService;
import com.hn.rbac.server.service.dao.CommonConfigMapper;
import com.hn.rbac.server.service.entity.CommonConfigDO;
import com.hn.rbac.server.service.entity.converter.CommonConfigConverter;
import com.hn.rbac.server.share.model.CommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CommonConfigCommandServiceImpl extends ServiceImpl<CommonConfigMapper, CommonConfigDO> implements CommonConfigCommandService {
    @Override
    public void saveConfig(CommonConfig commonConfig) {
        baseMapper.insert(CommonConfigConverter.INSTANCE.to(commonConfig));
    }
}
