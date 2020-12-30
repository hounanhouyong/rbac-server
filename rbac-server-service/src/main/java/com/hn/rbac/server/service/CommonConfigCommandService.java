package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.CommonConfigDO;
import com.hn.rbac.server.share.model.CommonConfig;

public interface CommonConfigCommandService extends IService<CommonConfigDO> {

    void saveConfig(CommonConfig commonConfig);
}
