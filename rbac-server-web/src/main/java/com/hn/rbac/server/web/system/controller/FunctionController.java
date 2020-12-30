package com.hn.rbac.server.web.system.controller;

import com.hn.rbac.server.common.constants.AdminConstant;
import com.hn.rbac.server.common.model.PageInfo;
import com.hn.rbac.server.common.result.Response;
import com.hn.rbac.server.service.FunctionCommandService;
import com.hn.rbac.server.service.FunctionQueryService;
import com.hn.rbac.server.service.MenuQueryService;
import com.hn.rbac.server.share.model.Function;
import com.hn.rbac.server.share.model.Menu;
import com.hn.rbac.server.share.request.enums.OrderByEnum;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import com.hn.rbac.server.share.request.query.FunctionQuery;
import com.hn.rbac.server.share.request.query.SortModel;
import com.hn.rbac.server.web.common.annotation.Log;
import com.hn.rbac.server.web.common.controller.BaseController;
import com.hn.rbac.server.web.common.exception.SystemException;
import com.hn.rbac.server.web.system.constants.SystemConstant;
import com.hn.rbac.server.web.system.request.FunctionRequest;
import com.hn.rbac.server.web.system.request.converter.FunctionRequestConverter;
import com.hn.rbac.server.web.system.vo.FunctionVO;
import com.hn.rbac.server.web.system.vo.converter.FunctionVOConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.Assert;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Validated
@RestController
@RequestMapping("/function")
public class FunctionController extends BaseController {

    @Resource
    private FunctionQueryService functionQueryService;
    @Resource
    private FunctionCommandService functionCommandService;
    @Resource
    private MenuQueryService menuQueryService;

    @Log("查看功能接口列表")
    @ResponseBody
    @GetMapping
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_FUNCTION_VIEW)
    public Response functionList(FunctionQuery functionQuery) {
        PageInfo<FunctionVO> functionVOPageInfo = new PageInfo<>(functionQuery.getPageNo(), functionQuery.getPageSize());
        // sort
        List<SortModel> sortModels = new ArrayList<>();
        sortModels.add(new SortModel("id", OrderByEnum.DESC.getValue()));
        PageInfo<Function> functionPageInfo = functionQueryService.findFunctionPage(functionQuery, sortModels);
        if (CollectionUtils.isEmpty(functionPageInfo.getData())) {
            return new Response().success(functionVOPageInfo);
        }
        List<Function> functions = functionPageInfo.getData();

        // appList
        List<Menu> appList = menuQueryService.findAllValidApps();
        if (CollectionUtils.isEmpty(appList)) {
            return new Response().success(functionVOPageInfo);
        }

        // appName
        Map<Long, String> appId2nameMap = appList.stream().collect(Collectors.toMap(Menu::getId, Menu::getMenuName));

        List<FunctionVO> functionVOList = new ArrayList<>();
        for (Function function : functions) {
            FunctionVO functionVO = FunctionVOConverter.INSTANCE.from(function);
            if (appId2nameMap.containsKey(function.getAppId())) {
                functionVO.setAppName(appId2nameMap.get(function.getAppId()));
            } else if (function.getAppId().equals(AdminConstant.MENU_ROOT_ID)) {
                functionVO.setAppName(AdminConstant.DEFAULT_APP_NAME);
            }
            functionVOList.add(functionVO);
        }

        functionVOPageInfo.setData(functionVOList);
        functionVOPageInfo.setTotalCount(functionPageInfo.getTotalCount());
        return new Response().success(functionVOPageInfo);
    }

    @Log("注册接口")
    @PostMapping
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_FUNCTION_ADD)
    public Response addFunction(@RequestBody FunctionRequest functionRequest) throws SystemException {
        Assert.isTrue(functionRequest != null && StringUtils.isNotEmpty(functionRequest.getFunctionName()), "Null functionName");
        Assert.isTrue(StringUtils.isNotEmpty(functionRequest.getFunctionCode()), "Null functionCode");
        // 验证功能接口名称重复问题及接口权限码重复问题
        if (functionRequest.getAppId() != null) {
            this.checkFunctionName(functionRequest.getAppId(), functionRequest.getFunctionName());
            this.checkFunctionCode(functionRequest.getAppId(), functionRequest.getFunctionCode());
        } else {
            this.checkFunctionName(0L, functionRequest.getFunctionName());
            this.checkFunctionCode(0L, functionRequest.getFunctionCode());
        }

        try {
            functionRequest.setOwner(getCurrentUsername());
            Long functionId = functionCommandService.createFunction(FunctionRequestConverter.INSTANCE.to(functionRequest));
            return new Response().success(functionId);
        } catch (Exception e) {
            log.error(SystemConstant.MSG_ADD_FUNCTION_FAIL, e);
            throw new SystemException(SystemConstant.MSG_ADD_FUNCTION_FAIL);
        }
    }

    @Log("禁用接口")
    @PutMapping("/disable/{functionId}")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_FUNCTION_DISABLE)
    public Response disabledFunction(@NotBlank(message = "{required}") @PathVariable String functionId) throws SystemException {
        try {
            functionCommandService.updateFunctionStatus(Long.parseLong(functionId), StatusEnum.INVALID);
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_DISABLED_FUNCTION_FAIL, e);
            throw new SystemException(SystemConstant.MSG_DISABLED_FUNCTION_FAIL);
        }
    }

    @Log("启用接口")
    @PutMapping("/enable/{functionId}")
//    @RequiresPermissions(SystemConstant.FUNCTION_CODE_FUNCTION_ENABLE)
    public Response enabledFunction(@NotBlank(message = "{required}") @PathVariable String functionId) throws SystemException {
        try {
            functionCommandService.updateFunctionStatus(Long.parseLong(functionId), StatusEnum.VALID);
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_ENABLED_FUNCTION_FAIL, e);
            throw new SystemException(SystemConstant.MSG_ENABLED_FUNCTION_FAIL);
        }
    }

    @Log("修改功能接口")
    @PutMapping("/update")
    @RequiresPermissions(SystemConstant.FUNCTION_CODE_FUNCTION_UPDATE)
    public Response updateFunction(@RequestBody FunctionRequest functionRequest) throws SystemException {
        Assert.isTrue(functionRequest != null && functionRequest.getId() != null, "Null functionId");
        // 验证是否存在, 不存在，直接返回成功
        Function oldFunction = functionQueryService.findValidFunctionById(functionRequest.getId());
        if (oldFunction == null) {
            return new Response().success();
        }

        // 验证功能接口名称重复问题及接口权限码重复问题
        if (functionRequest.getAppId() != null) {
            this.checkFunctionName(functionRequest.getAppId(), functionRequest.getFunctionName());
            this.checkFunctionCode(functionRequest.getAppId(), functionRequest.getFunctionCode());
        } else {
            this.checkFunctionName(oldFunction.getAppId(), functionRequest.getFunctionName());
            this.checkFunctionCode(oldFunction.getAppId(), functionRequest.getFunctionCode());
        }

        try {
            // 不支持相关数据项修改
            functionRequest.setFunctionCode(null);
            // 更新数据
            functionCommandService.updateFunction(FunctionRequestConverter.INSTANCE.to(functionRequest));
            return new Response().success();
        } catch (Exception e) {
            log.error(SystemConstant.MSG_UPDATE_FUNCTION_FAIL, e);
            throw new SystemException(SystemConstant.MSG_UPDATE_FUNCTION_FAIL);
        }
    }

    private void checkFunctionName(Long appId, String functionName) {
        if (functionQueryService.findValidFunctionByAppIdAndName(appId, functionName) != null) {
            throw new SystemException(String.format(SystemConstant.MSG_FUNCTION_NAME_EXIST, appId, functionName));
        }
    }

    private void checkFunctionCode(Long appId, String functionCode) {
        if (functionQueryService.findValidFunctionByAppIdAndCode(appId, functionCode) != null) {
            throw new SystemException(String.format(SystemConstant.MSG_FUNCTION_CODE_EXIST, functionCode));
        }
    }

}
