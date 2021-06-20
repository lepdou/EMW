/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.core.impl;

import com.google.common.collect.Maps;
import com.lepdou.framework.emw.config.starter.EMWConfigParams;
import com.lepdou.framework.emw.config.apollo.build.ApolloInjector;
import com.lepdou.framework.emw.config.apollo.util.ConfigUtil;
import com.lepdou.framework.emw.config.bean.ConfigDO;
import com.lepdou.framework.emw.config.core.ConfigLoader;
import com.lepdou.framework.emw.config.core.DBConfigManager;
import com.lepdou.framework.emw.config.core.DBConfigRepository;
import com.lepdou.framework.emw.config.utils.EMWThreadFactory;
import com.lepdou.framework.emw.config.utils.NamespaceKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author lepdou
 * @version : DefaultDBConfigManager.java, v 0.1 2021年06月10日 下午9:05 lepdou Exp $
 */
public class DefaultDBConfigManager implements DBConfigManager {
    private static final Logger logger                    = LoggerFactory.getLogger(DBConfigManager.class);
    private static final Random random                    = new Random();
    private static final String CONFIG_LOADER_THREAD_NAME = "EMWConfig";

    private ConfigLoader    configLoader;
    private EMWConfigParams params;
    private ConfigUtil      configUtil;

    private static DBConfigManager instance;

    private Map<String, DBConfigRepository> namespaceMapRepo = Maps.newConcurrentMap();
    private Map<String, Object>             namespaceMapLock = Maps.newConcurrentMap();

    public DefaultDBConfigManager(ConfigLoader configLoader, EMWConfigParams params) {
        this.configLoader = configLoader;
        this.params = params;
        instance = this;
        configUtil = ApolloInjector.getInstance(ConfigUtil.class);

        EMWThreadFactory.create(CONFIG_LOADER_THREAD_NAME, true).newThread(new LoadConfigWorker()).start();
    }

    public static DBConfigManager getInstance() {
        return instance;
    }

    @Override
    public void register(String namespace, DBConfigRepository configRepository) {
        String appId = configUtil.getAppId();
        String profile = configUtil.getCluster();
        String namespaceUniqueKey = NamespaceKeyUtils.genNamespaceUniqueKey(appId, namespace, profile);

        namespaceMapRepo.put(namespaceUniqueKey, configRepository);

        logger.info("Register namespace success. appId ={}, namespace = {}, profile = {}", appId, namespace, profile);
    }

    @Override
    public ConfigDO getConfig(String namespace) {
        Object lock = namespaceMapLock.computeIfAbsent(namespace, k -> new Object());

        synchronized (lock) {
            String appId = configUtil.getAppId();
            String profile = configUtil.getCluster();

            return configLoader.load(appId, namespace, profile);
        }
    }

    class LoadConfigWorker implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(params.getPollingInterval());
                } catch (InterruptedException e) {
                    //ignore
                }

                long startTime = System.currentTimeMillis();

                //日志采样比例设置为 1%
                boolean sampled = random.nextInt(100) < params.getPollingDBLogSampleRate();
                if (sampled) {
                    logger.info("Start load namespace from db. namespace size = {}", namespaceMapRepo.size());
                }

                doLoad();

                if (sampled) {
                    logger.info("Load namespace from db finished. namespace size = {}, duration = {} ms", namespaceMapRepo.size(),
                            (System.currentTimeMillis() - startTime));
                }
            }

        }

        private void doLoad() {
            for (Map.Entry<String, DBConfigRepository> entry : namespaceMapRepo.entrySet()) {
                String namespaceUniqueKey = entry.getKey();
                DBConfigRepository configRepository = entry.getValue();

                String[] namespaceInfo = NamespaceKeyUtils.parseFromNamespaceUniqueKey(namespaceUniqueKey);

                ConfigDO configDO = configLoader.load(namespaceInfo[0], namespaceInfo[1], namespaceInfo[2]);

                boolean updated = configRepository.tryUpdateConfig(configDO);

                if (updated) {
                    if (configDO != null) {
                        logger.info("Find updated namespace. appId = {}, namespace = {}, profile = {}, new version = {}, new value = {}",
                                configDO.getAppId(),
                                configDO.getNamespace(), configDO.getProfile(),
                                configDO.getVersion(), configDO.getValue());
                    } else {
                        logger.info("Find updated namespace. namespace = {}", namespaceUniqueKey);
                    }
                }
            }
        }
    }
}