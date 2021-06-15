/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.core;

import com.lepdou.framework.emw.config.bean.ConfigDO;


/**
 * 从数据库获取配置
 *
 * @author lepdou
 * @version : DBConfigLoader.java, v 0.1 2021年06月10日 下午8:30 lepdou Exp $
 */
public interface ConfigLoader {

    ConfigDO load(String namespace, String profile);

}