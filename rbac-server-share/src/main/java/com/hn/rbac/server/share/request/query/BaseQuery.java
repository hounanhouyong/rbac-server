package com.hn.rbac.server.share.request.query;

import com.hn.rbac.server.share.model.BaseModel;
import lombok.Data;

import java.util.List;

@Data
public class BaseQuery extends BaseModel {
    // 当前页面数据量
    private int pageSize = 10;
    // 当前页码
    private int pageNo = 1;

    // gmt_create > ${gtGmtCreate}
    private String gtGmtCreate;
    // gmt_create >= ${geGmtCreate}
    private String geGmtCreate;
    // gmt_create < ${gtGmtCreate}
    private String ltGmtCreate;
    // gmt_create <= ${geGmtCreate}
    private String leGmtCreate;
    // gmt_create > ${ltGmtCreate}
    private String gtGmtModified;
    // gmt_create >= ${leGmtCreate}
    private String geGmtModified;
    // gmt_create < ${ltGmtCreate}
    private String ltGmtModified;
    // gmt_create <= ${leGmtCreate}
    private String leGmtModified;

    private List<SortModel> sortModels;

}
