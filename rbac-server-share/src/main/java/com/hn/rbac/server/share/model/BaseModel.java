package com.hn.rbac.server.share.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseModel implements Serializable {
    public Long id;
    public Date gmtCreate;
    public Date gmtModified;
}
