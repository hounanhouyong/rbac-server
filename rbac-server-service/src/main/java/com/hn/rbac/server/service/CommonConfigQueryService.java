package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.CommonConfigDO;
import com.hn.rbac.server.share.model.CommonConfig;
import com.hn.rbac.server.share.request.query.CommonConfigQuery;

import java.util.List;

public interface CommonConfigQueryService extends IService<CommonConfigDO> {

    List<CommonConfig> findConfig(CommonConfigQuery query);
}
