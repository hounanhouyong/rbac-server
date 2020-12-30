package com.hn.rbac.server.service.entity.converter;

import com.hn.rbac.server.service.entity.PersistentLogDO;
import com.hn.rbac.server.share.model.PersistentLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersistentLogConverter {
    PersistentLogConverter INSTANCE = Mappers.getMapper(PersistentLogConverter.class);
    PersistentLogDO to(PersistentLog persistentLog);
}
