package com.hn.rbac.server.web.system.request.converter;

import com.hn.rbac.server.common.mapper.BasicObjectMapper;
import com.hn.rbac.server.share.model.Function;
import com.hn.rbac.server.web.system.request.FunctionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FunctionRequestConverter extends BasicObjectMapper<FunctionRequest, Function> {
    FunctionRequestConverter INSTANCE = Mappers.getMapper(FunctionRequestConverter.class);
}
