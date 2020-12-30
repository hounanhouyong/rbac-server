package com.hn.rbac.server.share.result;

import java.io.Serializable;

public class PageResult<T> extends ListResult<T> implements Serializable {

    private static final long serialVersionUID = -4175732524560165341L;
    private int pageNo;
    private int pageSize;
    private long totalCount;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

}