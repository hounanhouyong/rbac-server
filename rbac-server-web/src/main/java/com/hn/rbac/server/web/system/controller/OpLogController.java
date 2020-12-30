package com.hn.rbac.server.web.system.controller;

import com.hn.rbac.server.common.result.Response;
import com.hn.rbac.server.service.OpLogQueryService;
import com.hn.rbac.server.share.request.enums.OrderByEnum;
import com.hn.rbac.server.share.request.query.OpLogQuery;
import com.hn.rbac.server.share.request.query.SortModel;
import com.hn.rbac.server.web.common.annotation.Log;
import com.hn.rbac.server.web.system.constants.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/opLog")
public class OpLogController {

    @Resource
    private OpLogQueryService opLogQueryService;

    @Log("查看操作日志")
    @GetMapping
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_OPERATION_LOG_VIEW)
    public Response opLogList(OpLogQuery opLogQuery) {
        List<SortModel> sortModels = new ArrayList<>();
        sortModels.add(new SortModel("id", OrderByEnum.DESC.getValue()));
        return new Response().success(opLogQueryService.findOpLogPage(opLogQuery, sortModels));
    }

}
