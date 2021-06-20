/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.starter;

import com.lepdou.framework.emw.config.core.EMWConfigException;
import com.lepdou.framework.emw.config.core.ConfigDAO;
import com.lepdou.framework.emw.config.core.ConfigLoader;
import com.lepdou.framework.emw.config.core.impl.DefaultConfigLoader;
import com.lepdou.framework.emw.config.core.impl.DefaultConfigDAO;
import com.lepdou.framework.emw.config.core.impl.DefaultDBConfigManager;
import com.lepdou.framework.emw.config.facade.EMWConfigManagerFacade;
import com.lepdou.framework.emw.config.facade.EMWConfigManagerRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * 非 spring 环境下，使用此入口初始化模块
 * Example:
 *         String jdbcUrl = "jdbc:mysql://localhost:3306/emw";
 *         String username = "root";
 *         String password = "admin";
 *
 *         EMWConfigStarter.run(EMWConfigParams.builder().jdbcUrl(jdbcUrl).username(username).password(password).build());
 *
 *</pre>
 * @author lepdou
 * @version : EMWConfigStarter.java, v 0.1 2021年06月10日 下午8:25 lepdou Exp $
 */
public class EMWConfigStarter {
    private static final Logger logger = LoggerFactory.getLogger(EMWConfigStarter.class);

    private static volatile boolean initialized = false;

    public static void run(EMWConfigParams params) {
        if (!initialized) {
            synchronized (EMWConfigStarter.class) {
                if (!initialized) {
                    checkAndSetDefaultParams(params);

                    ConfigDAO configDAO = new DefaultConfigDAO(params.getJdbcUrl(), params.getUsername(), params.getPassword());
                    ConfigLoader configLoader = new DefaultConfigLoader(configDAO);
                    EMWConfigManagerFacade.setConfigDAO(configDAO);

                    new DefaultDBConfigManager(configLoader, params);

                    EMWConfigManagerRestController.start(8888);

                    initialized = true;

                    logger.info("EMW-Config initialize success. params = {}", params);
                }
            }
        }
    }

    private static void checkAndSetDefaultParams(EMWConfigParams params) {
        if (StringUtils.isEmpty(params.getJdbcUrl())) {
            String errorMsg = "EMW-Config initialize failed. because jdbc url is empty.";
            logger.error(errorMsg);
            throw new EMWConfigException(errorMsg);
        }

        if (StringUtils.isEmpty(params.getUsername())) {
            String errorMsg = "EMW-Config initialize failed. because username is empty.";
            logger.error(errorMsg);
            throw new EMWConfigException(errorMsg);
        }

        if (StringUtils.isEmpty(params.getPassword())) {
            String errorMsg = "EMW-Config initialize failed. because password is empty.";
            logger.error(errorMsg);
            throw new EMWConfigException(errorMsg);
        }

        if (params.getPollingDBLogSampleRate() == 0) {
            params.setPollingDBLogSampleRate(1);
        }

        if (params.getPollingInterval() == 0) {
            params.setPollingInterval(3000);
        }

        if (params.getRestPort() == 0) {
            params.setRestPort(18080);
        }
    }

}