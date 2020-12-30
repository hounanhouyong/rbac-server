package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.MenuDO;
import com.hn.rbac.server.share.model.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuConverter {
    MenuConverter INSTANCE = Mappers.getMapper(MenuConverter.class);
    Menu from(MenuDO menuDO);
    List<Menu> from(List<MenuDO> menuDOList);
    MenuDO to(Menu menu);
    List<MenuDO> to(List<Menu> menuList);
}
