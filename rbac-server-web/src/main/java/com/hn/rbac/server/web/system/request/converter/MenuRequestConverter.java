package com.hn.rbac.server.web.system.request.converter;

import com.hn.rbac.server.common.mapper.BasicObjectMapper;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.web.system.request.MenuRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuRequestConverter extends BasicObjectMapper<MenuRequest, Menu> {
    MenuRequestConverter INSTANCE = Mappers.getMapper(MenuRequestConverter.class);
}
