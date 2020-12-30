package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.CommonConfigDO;
import com.hn.rbac.server.share.model.CommonConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommonConfigConverter {
    CommonConfigConverter INSTANCE = Mappers.getMapper(CommonConfigConverter.class);
    CommonConfig from(CommonConfigDO commonConfigDO);
    List<CommonConfig> from(List<CommonConfigDO> commonConfigDOList);
    CommonConfigDO to(CommonConfig commonConfig);
    List<CommonConfigDO> to(List<CommonConfig> commonConfigList);
}
