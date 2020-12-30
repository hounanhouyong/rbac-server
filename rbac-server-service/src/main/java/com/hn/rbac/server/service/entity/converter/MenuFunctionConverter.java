package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.MenuFunctionDO;
import com.hn.rbac.server.share.model.MenuFunction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuFunctionConverter {
    MenuFunctionConverter INSTANCE = Mappers.getMapper(MenuFunctionConverter.class);
    MenuFunction from(MenuFunctionDO menuFunctionDO);
    List<MenuFunction> from(List<MenuFunctionDO> menuFunctionDOList);
}
