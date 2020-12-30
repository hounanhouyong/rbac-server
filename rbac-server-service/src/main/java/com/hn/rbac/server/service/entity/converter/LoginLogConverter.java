package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.LoginLogDO;
import com.hn.rbac.server.share.model.LoginLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoginLogConverter {
    LoginLogConverter INSTANCE = Mappers.getMapper(LoginLogConverter.class);
    LoginLogDO to(LoginLog loginLog);
}
