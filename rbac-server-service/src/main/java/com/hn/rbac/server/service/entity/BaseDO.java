package com.hn.rbac.server.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseDO implements Serializable {
    private static final long serialVersionUID = -1419469622993136679L;
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;
    public Date gmtCreate;
    public Date gmtModified;
}
