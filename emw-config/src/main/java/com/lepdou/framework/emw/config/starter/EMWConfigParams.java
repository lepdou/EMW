/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.starter;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 *
 * @author lepdou
 * @version : EMWConfigParams.java, v 0.1 2021年06月11日 上午10:11 lepdou Exp $
 */
@Data
@Builder
public class EMWConfigParams {

    /**
     * 轮询数据库间隔时间，单位毫秒。
     * 时间越短时效性越高，但是对于数据库的压力越大。
     */
    private int    pollingInterval;
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
     * 轮训数据库，日志打印采样比例。数值区间为[0~100]
     */
    private int pollingDBLogSampleRate;

    /**
     * rest http 服务端口
     */
    private int restPort;

}