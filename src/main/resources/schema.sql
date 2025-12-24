-- 如果表不存在则创建
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 项目表
CREATE TABLE IF NOT EXISTS `project` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` varchar(100) NOT NULL COMMENT '项目名称',
    `description` varchar(500) DEFAULT NULL COMMENT '项目描述',
    `owner_id` bigint(20) NOT NULL COMMENT '负责人ID(关联sys_user)',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

-- 任务表
    CREATE TABLE IF NOT EXISTS `task` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id` bigint(20) NOT NULL COMMENT '所属项目ID',
    `name` varchar(100) NOT NULL COMMENT '任务名称',
    `description` text DEFAULT NULL COMMENT '任务详情',
    `status` tinyint(1) DEFAULT 0 COMMENT '状态: 0-待办, 1-进行中, 2-已完成',
    `assignee_id` bigint(20) DEFAULT NULL COMMENT '指派人ID(关联sys_user)',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

 -- 项目成员关联表
     CREATE TABLE IF NOT EXISTS `project_member` (
         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
         `project_id` bigint(20) NOT NULL COMMENT '项目ID',
         `user_id` bigint(20) NOT NULL COMMENT '用户ID',
         `role` varchar(20) DEFAULT 'MEMBER' COMMENT '角色: OWNER-负责人, MEMBER-成员',
         `status` tinyint(1) DEFAULT 0 COMMENT '0:待确认, 1:已加入',
         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
         
         PRIMARY KEY (`id`),
         UNIQUE KEY `uk_project_user` (`project_id`, `user_id`) -- 保证一个人在一个项目里只能加入一次
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目成员表';