package com.hn.rbac.server.web.system.vo;

import com.hn.rbac.server.share.model.Role;
import com.hn.rbac.server.share.model.User;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class UserProfileVO implements Serializable {
    private static final long serialVersionUID = 3725734215224523294L;
    User user;
    List<Role> roles = new ArrayList<>();
    List<MenuVO> menus = new ArrayList<>();
}
