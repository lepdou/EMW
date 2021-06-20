/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.core;

import com.lepdou.framework.emw.config.bean.ConfigDO;

import java.util.List;

/**
 *
 * @author lepdou
 * @version : ConfigDAO.java, v 0.1 2021年06月10日 下午8:34 lepdou Exp $
 */
public interface ConfigDAO {

    ConfigDO findByNamespaceAndProfile(String appId, String namespace, String profile);

    List<ConfigDO> findAll();

    ConfigDO save(ConfigDO configDO);

    ConfigDO update(ConfigDO configDO);
}