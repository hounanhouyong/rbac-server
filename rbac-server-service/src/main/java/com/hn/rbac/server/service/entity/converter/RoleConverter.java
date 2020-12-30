package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.RoleDO;
import com.hn.rbac.server.share.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleConverter {
    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);
    RoleDO to(Role role);
    List<RoleDO> to(List<Role> roleList);
    Role from(RoleDO roleDO);
    List<Role> from(List<RoleDO> roleDOList);
}
