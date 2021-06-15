/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.core;

import com.lepdou.framework.emw.config.bean.ConfigDO;

/**
 * 注册 configRepository，定时从数据库捞取所有 namespace 的最新配置，如果有变更则触发回调事件
 *
 * @author lepdou
 * @version : ConfigManager.java, v 0.1 2021年06月10日 下午8:57 lepdou Exp $
 */
public interface DBConfigManager {

    void register(String namespace, DBConfigRepository configRepository);

    ConfigDO getConfig(String namespace);
}