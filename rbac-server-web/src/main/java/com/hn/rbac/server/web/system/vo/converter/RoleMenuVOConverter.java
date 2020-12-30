package com.hn.rbac.server.web.system.vo.converter;

import com.hn.rbac.server.share.model.RoleMenu;
import com.hn.rbac.server.web.system.vo.RoleMenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleMenuVOConverter {
    RoleMenuVOConverter INSTANCE = Mappers.getMapper(RoleMenuVOConverter.class);
    RoleMenuVO from(RoleMenu roleMenu);
    List<RoleMenuVO> from(List<RoleMenu> roleMenuList);
}
