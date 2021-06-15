/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.core;

import com.lepdou.framework.emw.config.apollo.enums.ConfigSourceType;
import com.lepdou.framework.emw.config.apollo.internals.AbstractConfigRepository;
import com.lepdou.framework.emw.config.apollo.internals.ConfigRepository;
import com.lepdou.framework.emw.config.bean.ConfigDO;
import com.lepdou.framework.emw.config.core.impl.DefaultDBConfigManager;
import com.lepdou.framework.emw.config.utils.EMWConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 *
 * @author lepdou
 * @version : DBConfigRepository.java, v 0.1 2021年06月10日 下午5:51 lepdou Exp $
 */
public class DBConfigRepository extends AbstractConfigRepository {
    private static final Logger logger = LoggerFactory.getLogger(DBConfigRepository.class);

    private DBConfigManager dbConfigManager;

    private          String   namespace;
    private volatile ConfigDO configDO;
    private volatile boolean  loaded;

    public DBConfigRepository(String namespace) {
        this.namespace = namespace;
        this.dbConfigManager = DefaultDBConfigManager.getInstance();

        if (dbConfigManager == null) {
            return;
        }

        dbConfigManager.register(namespace, this);
    }

    @Override
    protected void sync() {
        ConfigDO newConfigDO = dbConfigManager.getConfig(namespace);

        //1. 删除场景或者没有数据
        if (newConfigDO == null) {
            this.configDO = null;
            loaded = true;
            return;
        }

        //2. 没有变更
        if (configDO != null && newConfigDO.getVersion() == configDO.getVersion()) {
            return;
        }

        //3. 有变更
        String newValue = newConfigDO.getValue();

        newConfigDO.setProperties(EMWConfigUtils.parseToProperties(newValue));

        this.configDO = newConfigDO;

        loaded = true;
    }

    @Override
    public Properties getConfig() {
        if (configDO == null && !loaded) {
            sync();
        }

        if (configDO == null) {
            return new Properties();
        }

        Properties result = new Properties();
        result.putAll(configDO.getProperties());

        return result;
    }

    public boolean tryUpdateConfig(ConfigDO newConfig) {
        //1. 删除的场景
        if (configDO != null && newConfig == null) {
            fireRepositoryChange(namespace, new Properties());
            this.configDO = null;
            return true;
        }

        //2. 新增的场景
        if (configDO == null && newConfig != null) {
            newConfig.setProperties(EMWConfigUtils.parseToProperties(newConfig.getValue()));
            this.configDO = newConfig;

            fireRepositoryChange(namespace, newConfig.getProperties());

            return true;
        }

        //3. 更新的场景
        if (configDO != null && newConfig != null && newConfig.getVersion() > configDO.getVersion()) {
            newConfig.setProperties(EMWConfigUtils.parseToProperties(newConfig.getValue()));
            this.configDO = newConfig;

            fireRepositoryChange(namespace, newConfig.getProperties());

            return true;
        }

        return false;
    }

    @Override
    public void setUpstreamRepository(ConfigRepository upstreamConfigRepository) {

    }

    @Override
    public ConfigSourceType getSourceType() {
        return ConfigSourceType.DB;
    }
}