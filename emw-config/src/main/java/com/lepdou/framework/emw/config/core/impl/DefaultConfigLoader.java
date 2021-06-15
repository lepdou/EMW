/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.core.impl;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.lepdou.framework.emw.config.apollo.build.ApolloInjector;
import com.lepdou.framework.emw.config.apollo.util.ConfigUtil;
import com.lepdou.framework.emw.config.bean.ConfigDO;
import com.lepdou.framework.emw.config.core.ConfigDAO;
import com.lepdou.framework.emw.config.core.ConfigLoader;

import java.util.Objects;

/**
 *
 * @author lepdou
 * @version : DefaultConfigLoader.java, v 0.1 2021年06月10日 下午9:06 lepdou Exp $
 */
public class DefaultConfigLoader implements ConfigLoader {
    private ConfigDAO  configDAO;
    private ConfigUtil configUtil;

    public DefaultConfigLoader(ConfigDAO configDAO) {
        this.configDAO = configDAO;
        configUtil = ApolloInjector.getInstance(ConfigUtil.class);
    }

    @Override
    public ConfigDO load(String namespace, String profile) {
        ConfigDO configDO = configDAO.findByNamespaceAndProfile(namespace, profile);

        //如果按 profile 获取不到配置，则 fallback 到 default namespace 粒度
        if (configDO == null && !Objects.equals(configUtil.getCluster(), ConfigConsts.CLUSTER_NAME_DEFAULT)) {
            configDO = configDAO.findByNamespaceAndProfile(namespace, ConfigConsts.CLUSTER_NAME_DEFAULT);
        }

        return configDO;
    }

}