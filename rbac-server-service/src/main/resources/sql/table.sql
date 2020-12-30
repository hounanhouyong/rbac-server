create database rbac;

CREATE TABLE `common_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `group_id` varchar(64) DEFAULT NULL COMMENT '组id',
  `data_id` varchar(64) DEFAULT NULL COMMENT '数据id',
  `config_key` varchar(64) DEFAULT NULL COMMENT 'key',
  `config_value` varchar(64) DEFAULT NULL COMMENT 'value',
  `status` tinyint(1) DEFAULT '0',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='通用配置';


CREATE TABLE `function` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `app_id` bigint NOT NULL DEFAULT '0' COMMENT '应用ID',
  `function_code` varchar(128) NOT NULL COMMENT '功能权限码',
  `function_name` varchar(64) DEFAULT NULL,
  `interface_id` varchar(64) DEFAULT NULL,
  `owner` varchar(64) DEFAULT NULL COMMENT '权限码owner',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态,0-有效|1-无效',
  `remark` varchar(255) DEFAULT NULL COMMENT '功能权限说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='功能权限表';


CREATE TABLE `login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gmt_create` datetime NOT NULL COMMENT '登录时间',
  `gmt_modified` datetime DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `location` varchar(50) DEFAULT NULL COMMENT '登录地点',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='登录日志表';


CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `parent_id` bigint DEFAULT NULL COMMENT '上级菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单/按钮名称',
  `url` varchar(50) DEFAULT NULL COMMENT '菜单URL',
  `target_url` varchar(64) DEFAULT NULL,
  `icon` varchar(64) DEFAULT NULL,
  `perms` text COMMENT '权限标识',
  `type` varchar(20) NOT NULL COMMENT '类型 0菜单 1按钮',
  `order_num` bigint DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) DEFAULT '0',
  `remark` varchar(255) DEFAULT NULL,
  `parent_ids` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单表';


CREATE TABLE `menu_function` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `function_id` varchar(128) NOT NULL COMMENT '权限码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单功能权限关联表';


CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL COMMENT '操作用户',
  `module_id` bigint DEFAULT NULL,
  `module_name` varchar(64) DEFAULT NULL,
  `operation` text COMMENT '操作内容',
  `cost_times` decimal(11,0) DEFAULT NULL COMMENT '耗时',
  `method` text COMMENT '操作方法',
  `params` text COMMENT '方法参数',
  `ip` varchar(64) DEFAULT NULL COMMENT '操作者IP',
  `location` varchar(50) DEFAULT NULL COMMENT '操作地点',
  `access_from` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='操作日志表';


CREATE TABLE `persistent_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `obj_type` varchar(64) DEFAULT NULL COMMENT '对象类型',
  `obj_id` bigint DEFAULT NULL COMMENT '对象id',
  `current_value` varchar(1024) DEFAULT NULL COMMENT '修改前值',
  `modified_value` varchar(1024) DEFAULT NULL COMMENT '修改后值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='持久化日志';


CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `status` tinyint(1) DEFAULT '0',
  `type` varchar(20) DEFAULT 'admin',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';


CREATE TABLE `role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单/按钮ID',
  `all_selected` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';


CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `emp_id` bigint DEFAULT NULL,
  `emp_no` varchar(64) DEFAULT NULL,
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(64) DEFAULT NULL,
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `display_name` varchar(64) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0锁定 1有效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';


CREATE TABLE `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';
