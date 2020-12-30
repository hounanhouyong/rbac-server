package com.hn.rbac.server.share.model;

import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

@Data
public class User extends BaseModel {
    private static final long serialVersionUID = 5189156333923365504L;
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
     * 显示名称
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
