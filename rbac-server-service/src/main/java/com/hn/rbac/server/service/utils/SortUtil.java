package com.hn.rbac.server.service.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hn.rbac.server.share.request.enums.OrderByEnum;
import com.hn.rbac.server.share.request.query.SortModel;

import java.util.List;

/**
 * 处理排序工具类
 */
public class SortUtil {

    public static void handleWrapperSort(QueryWrapper<?> wrapper, List<SortModel> sortModels) {
        if (CollectionUtils.isNotEmpty(sortModels)) {
            for (SortModel sortModel : sortModels) {
                if (sortModel.getOrderItem().equals(OrderByEnum.DESC.getValue())) {
                    wrapper.orderByDesc(sortModel.getFieldName());
                } else {
                    wrapper.orderByAsc(sortModel.getFieldName());
                }
            }
        }
    }
}
