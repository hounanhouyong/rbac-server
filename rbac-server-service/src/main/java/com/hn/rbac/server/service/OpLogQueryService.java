package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.service.entity.OperationLogDO;
import com.hn.rbac.server.share.model.OperationLog;
import com.hn.rbac.server.share.request.query.OpLogQuery;
import com.hn.rbac.server.share.request.query.SortModel;

import java.util.List;

public interface OpLogQueryService extends IService<OperationLogDO> {

    Long findTotal(OpLogQuery opLogQuery, List<SortModel> sortModels);

    List<OperationLog> findOpLogs(OpLogQuery opLogQuery, List<SortModel> sortModels);

    PageInfo<OperationLog> findOpLogPage(OpLogQuery opLogQuery, List<SortModel> sortModels);
}
