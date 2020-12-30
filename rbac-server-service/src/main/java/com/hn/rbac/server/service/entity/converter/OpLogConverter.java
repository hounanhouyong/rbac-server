package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.OperationLogDO;
import com.hn.rbac.server.share.model.OperationLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OpLogConverter {
    OpLogConverter INSTANCE = Mappers.getMapper(OpLogConverter.class);
    OperationLog from(OperationLogDO opLogDO);
    List<OperationLog> from(List<OperationLogDO> opLogDoList);
    OperationLogDO to(OperationLog opLog);
    List<OperationLogDO> to(List<OperationLog> opLogList);
}
