package com.hn.rbac.server.share.check;

import com.hn.rbac.server.share.result.ListResult;
import com.hn.rbac.server.share.result.PageResult;
import com.hn.rbac.server.share.result.PlainResult;
import org.springframework.util.CollectionUtils;

public class ResultCheck {

    private static Boolean plainResultIsSuccess(PlainResult plainResult) {
        if (plainResult != null && plainResult.isSuccess()) {
            return true;
        }
        return false;
    }

    private static Boolean plainResultHasData(PlainResult plainResult) {
        if (plainResult != null && plainResult.isSuccess() && plainResult.getData() != null) {
            return true;
        }
        return false;
    }

    private static Boolean listResultIsSuccess(ListResult listResult) {
        if (listResult != null && listResult.isSuccess()) {
            return true;
        }
        return false;
    }

    private static Boolean listResultHasData(ListResult listResult) {
        if (listResult != null && listResult.isSuccess() && !CollectionUtils.isEmpty(listResult.getData())) {
            return true;
        }
        return false;
    }

    private static Boolean pageResultIsSuccess(PageResult pageResult) {
        if (pageResult != null && pageResult.isSuccess()) {
            return true;
        }
        return false;
    }

    private static Boolean pageResultHasData(PageResult pageResult) {
        if (pageResult != null && pageResult.isSuccess() && !CollectionUtils.isEmpty(pageResult.getData())) {
            return true;
        }
        return false;
    }

}
