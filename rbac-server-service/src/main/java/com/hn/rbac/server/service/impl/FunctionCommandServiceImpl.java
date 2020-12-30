package com.hn.rbac.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hn.rbac.server.service.FunctionCommandService;
import com.hn.rbac.server.service.PersistentLogCommandService;
import com.hn.rbac.server.service.dao.FunctionMapper;
import com.hn.rbac.server.service.entity.FunctionDO;
import com.hn.rbac.server.service.entity.converter.FunctionConverter;
import com.hn.rbac.server.share.model.Function;
import com.hn.rbac.server.share.model.PersistentLog;
import com.hn.rbac.server.share.request.enums.PersistentObjTypeEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class FunctionCommandServiceImpl extends ServiceImpl<FunctionMapper, FunctionDO> implements FunctionCommandService {

    @Resource
    private PersistentLogCommandService persistentLogCommandService;

    @Override
    public Long createFunction(Function function) {
        Date now = new Date();
        function.setGmtCreate(now);
        function.setGmtModified(now);
        function.setStatus(StatusEnum.VALID.getValue());
        if (function.getAppId() == null) {
            function.setAppId(0L);
        }
        FunctionDO functionDO = FunctionConverter.INSTANCE.to(function);
        this.save(functionDO);

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.FUNCTION.getValue())
                                                                        .objId(functionDO.getId())
                                                                        .modifiedValue(JSON.toJSONString(functionDO))
                                                                        .build());

        return functionDO.getId();
    }

    @Override
    public void updateFunction(Function function) throws Exception {
        Assert.isTrue(function != null && function.getId() != null, "Null functionId");
        function.setGmtModified(new Date());
        FunctionDO functionDO = FunctionConverter.INSTANCE.to(function);
        this.baseMapper.updateById(functionDO);

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.FUNCTION.getValue())
                                                                        .objId(functionDO.getId())
                                                                        .modifiedValue(JSON.toJSONString(functionDO))
                                                                        .build());

    }

    @Override
    public void updateFunctionStatus(Long functionId, StatusEnum statusEnum) throws Exception {
        Assert.isTrue(functionId != null, "Null functionId");
        FunctionDO functionDO = new FunctionDO();
        functionDO.setId(functionId);
        functionDO.setStatus(statusEnum.getValue());
        functionDO.setGmtModified(new Date());
        this.baseMapper.updateById(functionDO);

        // 变更记录持久化
        persistentLogCommandService.savePersistentLog(PersistentLog.builder()
                                                                        .objType(PersistentObjTypeEnum.FUNCTION.getValue())
                                                                        .objId(functionDO.getId())
                                                                        .modifiedValue(JSON.toJSONString(functionDO))
                                                                        .build());
    }
}
