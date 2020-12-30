package com.hn.rbac.server.web.system.vo.converter;

import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.web.system.vo.MenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuVOConverter {
    MenuVOConverter INSTANCE = Mappers.getMapper(MenuVOConverter.class);
    MenuVO from(Menu menu);
    List<MenuVO> from(List<Menu> menuList);
}
