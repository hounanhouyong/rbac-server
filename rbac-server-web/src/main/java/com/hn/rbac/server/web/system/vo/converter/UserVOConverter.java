package com.hn.rbac.server.web.system.vo.converter;

import com.hn.rbac.server.share.model.User;
import com.hn.rbac.server.web.system.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserVOConverter {
    UserVOConverter INSTANCE = Mappers.getMapper(UserVOConverter.class);
    UserVO from(User user);
    List<UserVO> from(List<User> users);
}
