package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.service.entity.PersistentLogDO;
import com.hn.rbac.server.share.model.PersistentLog;
import org.springframework.scheduling.annotation.Async;

public interface PersistentLogCommandService extends IService<PersistentLogDO> {

    /**
     * 异步保存持久化日志
     */
    @Async(AdminConstant.ASYNC_POOL)
    void savePersistentLog(PersistentLog persistentLog);
}
