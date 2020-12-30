package com.hn.rbac.server.common.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageInfo<T> {
    private long pageNo = 1;
    private long pageSize = 20;
    private long totalCount = 0;
    private List<T> data = new ArrayList<>();

    public PageInfo(long pageNo, long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
