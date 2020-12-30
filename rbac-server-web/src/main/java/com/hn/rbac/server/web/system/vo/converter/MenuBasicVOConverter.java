package com.hn.rbac.server.web.system.vo.converter;

import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.web.system.vo.MenuBasicVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuBasicVOConverter {
    MenuBasicVOConverter INSTANCE = Mappers.getMapper(MenuBasicVOConverter.class);
    MenuBasicVO from(Menu menu);
    List<MenuBasicVO> from(List<Menu> menuList);
}
