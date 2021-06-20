CREATE TABLE `emw_config` (
                              `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
                              `app_id` varchar(255) DEFAULT 'default' COMMENT '应用名',
                              `namespace` varchar(255) NOT NULL DEFAULT '' COMMENT 'namespace名称',
                              `profile` varchar(128) NOT NULL DEFAULT 'default' COMMENT 'namespace 不同的版本',
                              `value` longtext NOT NULL COMMENT 'properties 文件内容',
                              `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
                              `context` varchar(10240) DEFAULT NULL COMMENT '上下文信息',
                              `grayRules` varchar(10240) DEFAULT NULL COMMENT '灰度规则',
                              `gmt_create` date DEFAULT NULL COMMENT '创建时间',
                              `gmt_modified` date DEFAULT NULL COMMENT '最后更新时间',
                              `operator` varchar(64) DEFAULT NULL COMMENT '最后操作人',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_app_namespace_cluster` (`app_id`,`namespace`,`profile`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='配置文件表';