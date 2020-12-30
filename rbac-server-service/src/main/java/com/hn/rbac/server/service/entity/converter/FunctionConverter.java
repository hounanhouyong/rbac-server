package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.FunctionDO;
import com.hn.rbac.server.share.model.Function;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FunctionConverter {
    FunctionConverter INSTANCE = Mappers.getMapper(FunctionConverter.class);
    Function from(FunctionDO functionDO);
    List<Function> from(List<FunctionDO> functionDOList);
    FunctionDO to(Function function);
    List<FunctionDO> to(List<Function> functionList);
}
