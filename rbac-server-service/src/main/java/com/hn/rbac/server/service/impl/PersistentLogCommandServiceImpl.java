package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.PersistentLogCommandService;
import com.hn.rbac.server.service.dao.PersistentLogMapper;
import com.hn.rbac.server.service.entity.PersistentLogDO;
import com.hn.rbac.server.service.entity.converter.PersistentLogConverter;
import com.hn.rbac.server.share.model.PersistentLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class PersistentLogCommandServiceImpl extends ServiceImpl<PersistentLogMapper, PersistentLogDO> implements PersistentLogCommandService {

    @Override
    public void savePersistentLog(PersistentLog persistentLog) {
        Date current = new Date();
        persistentLog.setGmtCreate(current);
        persistentLog.setGmtModified(current);
        baseMapper.insert(PersistentLogConverter.INSTANCE.to(persistentLog));
    }
}
