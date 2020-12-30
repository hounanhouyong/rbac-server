package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.UserDO;
import com.hn.rbac.server.share.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);
    User from(UserDO userDO);
    List<User> from(List<UserDO> userDOList);
    UserDO to(User user);
    List<UserDO> to(List<User> userList);
}
