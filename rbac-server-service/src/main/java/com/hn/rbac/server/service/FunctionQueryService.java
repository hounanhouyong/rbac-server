package com.hn.rbac.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.service.entity.FunctionDO;
import com.hn.rbac.server.share.model.Function;
import com.hn.rbac.server.share.request.query.FunctionQuery;
import com.hn.rbac.server.share.request.query.SortModel;

import java.util.List;

public interface FunctionQueryService extends IService<FunctionDO> {

    Long findTotal(FunctionQuery functionQuery, List<SortModel> sortModels);

    List<Function> findFunctions(FunctionQuery functionQuery, List<SortModel> sortModels);

    PageInfo<Function> findFunctionPage(FunctionQuery functionQuery, List<SortModel> sortModels);

    Function findValidFunctionById(Long functionId);

    List<Function> findValidFunctionByBatchIds(List<Long> functionIdList);

    List<String> findValidFunctionCodeByBatchIds(List<Long> functionIdList);

    Function findValidFunctionByAppIdAndName(Long appId, String functionName);

    Function findValidFunctionByAppIdAndCode(Long appId, String functionCode);

    Function findValidFunctionByCode(String functionCode);

    List<String> findFunctionCodeList(String username);

    List<String> findAllValidFunctionCodeList();

}
