package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.service.entity.FunctionDO;
import com.hn.rbac.server.share.model.Function;
import com.hn.rbac.server.share.request.enums.StatusEnum;

public interface FunctionCommandService extends IService<FunctionDO> {

    Long createFunction(Function function);

    void updateFunction(Function function) throws Exception;

    void updateFunctionStatus(Long functionId, StatusEnum statusEnum) throws Exception;
}
