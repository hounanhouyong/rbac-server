package com.hn.rbac.server.web.system.vo;

import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.share.request.enums.StatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 5406084737647645801L;
    /**
     * ID
     */
    private Long id;
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
    /**
     * 角色
     */
    private List<Role> roleList = new ArrayList<>();
}
