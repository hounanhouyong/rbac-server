package com.hn.rbac.server.web.system.request.converter;

import com.hn.rbac.server.common.mapper.BasicObjectMapper;
import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.web.system.request.RoleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleRequestConverter extends BasicObjectMapper<RoleRequest, Role> {
    RoleRequestConverter INSTANCE = Mappers.getMapper(RoleRequestConverter.class);
}
