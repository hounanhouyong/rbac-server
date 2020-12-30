package com.hn.rbac.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.rbac.server.service.OpLogCommandService;
import com.hn.rbac.server.service.dao.OperationLogMapper;
import com.hn.rbac.server.service.entity.OperationLogDO;
import com.hn.rbac.server.service.entity.converter.OpLogConverter;
import com.hn.rbac.server.share.model.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OpLogCommandServiceImpl extends ServiceImpl<OperationLogMapper, OperationLogDO> implements OpLogCommandService {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Long saveLog(OperationLog opLog) {
        Date current = new Date();
        opLog.setGmtCreate(current);
        opLog.setGmtModified(current);
        OperationLogDO opLogDO = OpLogConverter.INSTANCE.to(opLog);
        baseMapper.insert(opLogDO);
        return opLogDO.getId();
    }

    @Transactional
    @Override
    public void saveLog(OperationLog opLog, Object[] args, String[] paramNames) {
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            opLog.setParams(params.toString());
            if (StringUtils.isNotBlank(params) && params.length() > 20000) {
                log.info("optLog={}", opLog);
                opLog.setParams(params.toString().substring(0, 20000));
            }
        }
        // 保存系统日志
        baseMapper.insert(OpLogConverter.INSTANCE.to(opLog));
    }

    @SuppressWarnings("all")
    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Map) {
                    Set set = ((Map) args[i]).keySet();
                    List<Object> list = new ArrayList<>();
                    List<Object> paramList = new ArrayList<>();
                    for (Object key : set) {
                        list.add(((Map) args[i]).get(key));
                        paramList.add(key);
                    }
                    return handleParams(params, list.toArray(), paramList);
                } else {
                    if (args[i] instanceof Serializable) {
                        Class<?> aClass = args[i].getClass();
                        try {
                            aClass.getDeclaredMethod("toString", new Class[]{null});
                            // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                            params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
                        } catch (NoSuchMethodException e) {
                            params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
                        }
                    } else if (args[i] instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) args[i];
                        params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                    } else {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                    }
                }
            }
        } catch (Exception ignore) {
            params.append("参数解析失败");
        }
        return params;
    }

}
