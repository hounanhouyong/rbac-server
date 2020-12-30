package com.hn.rbac.server.share.result;

import java.io.Serializable;
import java.util.List;

public class ListResult<T> extends BaseResult implements Serializable {

    private static final long serialVersionUID = 3957675638859096351L;

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
    
    public static <T> ListResult<T> success(List<T> data) {
        ListResult<T> result = new ListResult<T>();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }
}
