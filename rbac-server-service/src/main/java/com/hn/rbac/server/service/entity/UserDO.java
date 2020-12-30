package com.hn.rbac.server.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;


@Data
@TableName("user")
public class UserDO extends BaseDO {
    /**
     * 员工ID
     */
    private Long empId;
    /**
     * 员工工号
     */
    private String empNo;
    /**
     * 部门 ID
     */
    private Long deptId;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String displayName;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 状态
     * @see StatusEnum
     */
    private Integer status;
}
