/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.spring;

/**
 *
 * @author lepdou
 * @version : EMWConfigConstants.java, v 0.1 2021年06月16日 下午7:37 lepdou Exp $
 */
public interface EMWConfigConstants {

    String EMW_CONFIG_BOOTSTRAP_ENABLED             = "emw.emw-config.bootstrap.enabled";
    String EMW_CONFIG_BOOTSTRAP_JDBC_URL            = "emw.emw-config.bootstrap.jdbc.url";
    String EMW_CONFIG_BOOTSTRAP_JDBC_USERNAME       = "emw.emw-config.bootstrap.jdbc.username";
    String EMW_CONFIG_BOOTSTRAP_JDBC_PASSWORD       = "emw.emw-config.bootstrap.jdbc.password";
    String EMW_CONFIG_BOOTSTRAP_POLLING_DB_LOG_RATE = "emw.emw-config.bootstrap.pollingDBLogSampleRate";
    String EMW_CONFIG_BOOTSTRAP_POLLING_INTERVAL    = "emw.emw-config.bootstrap.pollingInterval";
    String EMW_CONFIG_BOOTSTRAP_REST_PORT           = "emw.emw-config.bootstrap.restPort";

    String EMW_CONFIG_PARAM_JDBC_URL            = "jdbcUrl";
    String EMW_CONFIG_PARAM_JDBC_USERNAME       = "username";
    String EMW_CONFIG_PARAM_JDBC_PASSWORD       = "password";
    String EMW_CONFIG_PARAM_POLLING_DB_LOG_RATE = "pollingDBLogSampleRate";
    String EMW_CONFIG_PARAM_POLLING_INTERVAL    = "pollingInterval";
    String EMW_CONFIG_PARAM_REST_PORT           = "restPort";
}