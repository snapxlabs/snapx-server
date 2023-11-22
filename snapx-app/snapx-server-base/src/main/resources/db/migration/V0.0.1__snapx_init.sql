CREATE TABLE IF NOT EXISTS `sys_admin_user`
(
    `id` BIGINT UNSIGNED NOT NULL COMMENT '主键',
    `name` VARCHAR(16) NOT NULL COMMENT '用户姓名',
    `username` VARCHAR(32) NOT NULL COMMENT '用户账号',
    `password` VARCHAR(255) NULL COMMENT '用户密码',
    `phone` VARCHAR(32) NULL COMMENT '手机号',
    `mail` VARCHAR(64) NULL COMMENT '电子邮箱',
    `remark` VARCHAR(255) NULL COMMENT '备注信息',
    `enabled` TINYINT(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '停用/启用标识；0 停用；1启用',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    `create_by` BIGINT UNSIGNED NOT NULL COMMENT '创建操作人id',
    `update_by` BIGINT UNSIGNED NOT NULL COMMENT '更新操作人id',
    `deleted` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间，值为0则未删除',
    PRIMARY KEY (`id`),
    UNIQUE (`username`, `deleted`),
    UNIQUE (`phone`, `deleted`),
    UNIQUE (`mail`, `deleted`)
) ENGINE = InnoDB DEFAULT CHARSET utf8mb4 COMMENT '系统管理员';

CREATE TABLE IF NOT EXISTS `inf_resource_file`
(
    `id` BIGINT UNSIGNED NOT NULL COMMENT '主键',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `file_extension` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '文件扩展名',
    `file_size` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '文件大小，字节数',
    `content_type` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '文件的http协议Content-Type',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    `create_by` BIGINT UNSIGNED NOT NULL COMMENT '创建操作人id',
    `update_by` BIGINT UNSIGNED NOT NULL COMMENT '更新操作人id',
    `deleted` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除时间，值为0则未删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET utf8mb4 COMMENT '上传文件记录';

CREATE TABLE `cam_camera` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '相机名称',
  `code` varchar(20) NOT NULL COMMENT '相机编号',
  `picture_url` varchar(255) NOT NULL COMMENT '相机图片地址',
  `luck` BIGINT UNSIGNED DEFAULT NULL COMMENT '幸运属性',
  `efficiency` BIGINT UNSIGNED DEFAULT NULL COMMENT '效率属性',
  `resilience` BIGINT UNSIGNED DEFAULT NULL COMMENT '恢复属性',
  `stability` BIGINT UNSIGNED DEFAULT NULL COMMENT '稳定属性',
  `battery` BIGINT UNSIGNED DEFAULT NULL COMMENT '电池属性',
  `memory` BIGINT UNSIGNED DEFAULT NULL COMMENT '内存属性',
  `mint` BIGINT UNSIGNED DEFAULT NULL COMMENT '货币属性',
  `maintain` BIGINT UNSIGNED DEFAULT NULL COMMENT '保养属性',
  `is_gift` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否是陪玩，0 不是 1是',
  `instruction` varchar(500) DEFAULT NULL COMMENT '相机描述介绍',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序，越大越靠前',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint unsigned NOT NULL COMMENT '创建操作人id',
  `update_by` bigint unsigned NOT NULL COMMENT '更新操作人id',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除时间，值为0则未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='相机信息';

CREATE TABLE `cam_camera_member` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `camera_id` bigint unsigned NOT NULL COMMENT '相机Id',
  `member_id` bigint unsigned NOT NULL COMMENT '会员Id',
  `current_level` BIGINT UNSIGNED DEFAULT 0 COMMENT '相机等级',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint unsigned NOT NULL COMMENT '创建操作人id',
  `update_by` bigint unsigned NOT NULL COMMENT '更新操作人id',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除时间，值为0则未删除',
  PRIMARY KEY (`id`),
  INDEX `INDEX_MEMBER_ID` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员相机信息';

CREATE TABLE `cam_film_member` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `member_id` bigint unsigned NOT NULL COMMENT '会员Id',
  `remaining_quantity` BIGINT UNSIGNED DEFAULT 0 COMMENT '可用数量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint unsigned NOT NULL COMMENT '创建操作人id',
  `update_by` bigint unsigned NOT NULL COMMENT '更新操作人id',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除时间，值为0则未删除',
  PRIMARY KEY (`id`),
  INDEX `INDEX_MEMBER_ID` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员胶卷信息';

CREATE TABLE `cam_film_member_detail` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `member_id` bigint unsigned NOT NULL COMMENT '会员Id',
  `film_change_type` varchar(50) NOT NULL COMMENT '来源类型',
  `variable_quantity` BIGINT NOT NULL  COMMENT '变动数量 正数表示增加 负数表示减少',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint unsigned NOT NULL COMMENT '创建操作人id',
  `update_by` bigint unsigned NOT NULL COMMENT '更新操作人id',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除时间，值为0则未删除',
  PRIMARY KEY (`id`),
  INDEX `INDEX_MEMBER_ID` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员胶卷详情信息';

CREATE TABLE `mem_member_interaction` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `member_id` bigint unsigned NOT NULL COMMENT '会员Id',
  `is_follow_twitter` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否关注Twitter，0 不是 1是',
  `follow_twitter_time` datetime DEFAULT NULL  COMMENT '关注时间',
  `is_join_discord` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否加入社区，0 不是 1是',
  `join_discord_time` datetime DEFAULT NULL  COMMENT '加入时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_by` bigint unsigned NOT NULL COMMENT '创建操作人id',
  `update_by` bigint unsigned NOT NULL COMMENT '更新操作人id',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '删除时间，值为0则未删除',
  PRIMARY KEY (`id`),
  INDEX `INDEX_MEMBER_ID` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员互动信息';


