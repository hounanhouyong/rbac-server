package com.hn.rbac.server.web.system.vo.converter;

import com.hn.rbac.server.share.model.Function;
import com.hn.rbac.server.web.system.vo.FunctionVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FunctionVOConverter {
    FunctionVOConverter INSTANCE = Mappers.getMapper(FunctionVOConverter.class);
    FunctionVO from(Function function);
    List<FunctionVO> from(List<Function> functionList);
}
