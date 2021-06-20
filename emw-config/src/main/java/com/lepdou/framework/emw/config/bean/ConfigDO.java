/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Properties;

/**
 *
 * @author lepdou
 * @version : ConfigDO.java, v 0.1 2021年06月10日 下午9:15 lepdou Exp $
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDO {
    public static final String FIELD_ID            = "id";
    public static final String FIELD_APP_ID        = "app_id";
    public static final String FIELD_NAMESPACE     = "namespace";
    public static final String FIELD_PROFILE       = "profile";
    public static final String FIELD_VALUE         = "value";
    public static final String FIELD_VERSION       = "version";
    public static final String FIELD_CONTEXT       = "context";
    public static final String FIELD_GRAY_RULES    = "grayRules";
    public static final String FIELD_CREATE_TIME   = "gmt_create";
    public static final String FIELD_MODIFIED_TIME = "gmt_modified";
    public static final String FIELD_OPERATOR      = "operator";

    private long   id;
    /**
     *因为有时候多个应用共用数据库，所以通过 appId 隔离不用应用的配置。
     */
    private String appId;
    /**
     * namespace 相当于一个配置文件的文件名
     */
    private String namespace;
    /**
     * 一个namespace可能存在多个版本，通过 profile 来激活某个版本
     */
    private String profile;
    /**
     * 配置文件内容
     */
    private String value;
    /**
     * 配置文件版本号，通过 version 来做乐观锁
     */
    private int    version;
    /**
     * 上下文信息
     */
    private String context;
    /**
     * 配置下发往往需要一系列的灰度策略，例如按 IP 灰度
     */
    private String grayRules;

    private Date   gmtCreateTime;
    private Date   gmtLastModifiedTime;
    private String operator;

    /**
     * 如果是 properties 格式，缓存解析后的 properties 对象
     */
    private Properties properties;

}