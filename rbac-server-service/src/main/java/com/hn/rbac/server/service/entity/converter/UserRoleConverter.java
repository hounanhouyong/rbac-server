package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.UserRoleDO;
import com.hn.rbac.server.share.model.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserRoleConverter {
    UserRoleConverter INSTANCE = Mappers.getMapper(UserRoleConverter.class);
    UserRole from(UserRoleDO userRoleDO);
    List<UserRole> from(List<UserRoleDO> userRoleDOList);
    UserRoleDO to(UserRole userRole);
}
