package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.RoleMenuDO;
import com.hn.rbac.server.share.model.RoleMenu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleMenuConverter {
    RoleMenuConverter INSTANCE = Mappers.getMapper(RoleMenuConverter.class);
    RoleMenu from(RoleMenuDO roleMenuDO);
    List<RoleMenu> from(List<RoleMenuDO> roleMenuDOList);
    RoleMenuDO to(RoleMenu roleMenu);
}
