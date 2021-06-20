/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.starter;

import org.springframework.util.StringUtils;

import java.util.Map;

/**
 *
 * @author lepdou
 * @version : EMWConfigParams.java, v 0.1 2021年06月11日 上午10:11 lepdou Exp $
 */
public class EMWConfigParams {

    /**
     * 数据库连接串
     */
    private String jdbcUrl;
    /**
     * 数据库账户
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;

    /**
     * 当前进程的标签信息，用于灰度策略匹配
     */
    private Map<String, String> labels;

    /**
     * 轮询数据库间隔时间，单位毫秒。
     * 时间越短时效性越高，但是对于数据库的压力越大。
     */
    private int pollingInterval;

    /**
     * 轮训数据库，日志打印采样比例。数值区间为[0~100]
     */
    private int pollingDBLogSampleRate;

    /**
     * rest http 服务端口
     */
    private int restPort;

    public EMWConfigParams() {
    }

    public static EMWConfigParams createFromBaseParams(String jdbcUrl, String username, String password) {
        EMWConfigParams params = new EMWConfigParams();
        params.setJdbcUrl(jdbcUrl);
        params.setUsername(username);
        params.setPassword(password);
        return params;
    }

    public static EMWConfigParams createFromOptionalStringParams(String jdbcUrl, String username, String password, String pollingInterval,
                                                                 String pollingDBLogSampleRate, String restPort) {
        EMWConfigParams params = new EMWConfigParams();
        params.setJdbcUrl(jdbcUrl);
        params.setUsername(username);
        params.setPassword(password);
        if (!StringUtils.isEmpty(pollingInterval)) {
            params.setPollingInterval(Integer.parseInt(pollingInterval));
        }
        if (!StringUtils.isEmpty(pollingDBLogSampleRate)) {
            params.setPollingDBLogSampleRate(Integer.parseInt(pollingDBLogSampleRate));
        }
        if (!StringUtils.isEmpty(restPort)) {
            params.setRestPort(Integer.parseInt(restPort));
        }
        return params;
    }

    public static EMWConfigParams createFromOptionalParams(String jdbcUrl, String username, String password, int pollingInterval,
                                                           int pollingDBLogSampleRate, int restPort) {
        EMWConfigParams params = new EMWConfigParams();
        params.setJdbcUrl(jdbcUrl);
        params.setUsername(username);
        params.setPassword(password);
        params.setRestPort(restPort);
        params.setPollingDBLogSampleRate(pollingDBLogSampleRate);
        params.setPollingInterval(pollingInterval);
        return params;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public int getPollingDBLogSampleRate() {
        return pollingDBLogSampleRate;
    }

    public void setPollingDBLogSampleRate(int pollingDBLogSampleRate) {
        this.pollingDBLogSampleRate = pollingDBLogSampleRate;
    }

    public int getRestPort() {
        return restPort;
    }

    public void setRestPort(int restPort) {
        this.restPort = restPort;
    }
}