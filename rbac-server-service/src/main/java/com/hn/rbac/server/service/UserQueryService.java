package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.service.entity.UserDO;
import com.hn.rbac.server.share.model.User;
import com.hn.rbac.server.share.request.query.SortModel;
import com.hn.rbac.server.share.request.query.UserQuery;

import java.util.List;

public interface UserQueryService extends IService<UserDO> {

    Long findTotal(UserQuery userQuery, List<SortModel> sortModels);

    List<User> findUsers(UserQuery userQuery, List<SortModel> sortModels);

    PageInfo<User> findUserPage(UserQuery userQuery, List<SortModel> sortModels);

    User findByName(String username);

    User findValidUserByName(String username);

    User findById(Long id);

    User findValidUserById(Long id);

    User findValidUserByEmpId(Long empId);

    List<User> findByBatchIds(List<Long> idList);

    List<User> findValidUsersByBatchIds(List<Long> idList);

    List<String> findAllUsername();

}
