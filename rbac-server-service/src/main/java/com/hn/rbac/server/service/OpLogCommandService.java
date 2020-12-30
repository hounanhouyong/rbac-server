package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.service.entity.OperationLogDO;
import com.hn.rbac.server.share.model.OperationLog;
import org.springframework.scheduling.annotation.Async;

public interface OpLogCommandService extends IService<OperationLogDO> {

    Long saveLog(OperationLog opLog);

    /**
     * 异步保存操作日志
     */
    @Async(AdminConstant.ASYNC_POOL)
    void saveLog(OperationLog opLog, Object[] args, String[] paramNames);
}
