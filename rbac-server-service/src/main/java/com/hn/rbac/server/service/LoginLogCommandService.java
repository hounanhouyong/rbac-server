package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.LoginLogDO;
import com.hn.rbac.server.share.model.LoginLog;

public interface LoginLogCommandService extends IService<LoginLogDO> {

    void saveLoginLog(LoginLog loginLog);
}
