/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.starter;

import com.lepdou.framework.emw.config.spring.EMWConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <pre>
 * SpringBoot 模式下的初始化入口.
 *
 * 需要在 application.yaml 里配置，例子：
 * emw:
 *   emw-config:
 *     bootstrap:
 *       jdbc:
 *         url: jdbc:mysql://localhost:3306/emw
 *         username: root
 *         password: admin
 * </pre>
 *
 *
 * @author lepdou
 * @version : EMWConfigContextInitializer.java, v 0.1 2021年06月16日 下午7:35 lepdou Exp $
 */
public class EMWConfigContextInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext>, EnvironmentPostProcessor, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(EMWConfigContextInitializer.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();

        boolean enabled = Boolean.parseBoolean(environment.getProperty(EMWConfigConstants.EMW_CONFIG_BOOTSTRAP_ENABLED, "false"));

        if (!enabled) {
            logger.info("emw-config is disabled. {} = false", EMWConfigConstants.EMW_CONFIG_BOOTSTRAP_ENABLED);
        } else {
            logger.info("emw-config is enabled and start to init emw-config module. {} = true",
                    EMWConfigConstants.EMW_CONFIG_BOOTSTRAP_ENABLED);
        }

        if (environment.getPropertySources().contains(EMWConfigConstants.EMW_CONFIG_BOOTSTRAP_ENABLED)) {
            //already initialized
            return;
        }

        String jdbcUrl = environment.getProperty(EMWConfigConstants.EMW_CONFIG_BOOTSTRAP_JDBC_URL);
        String username = environment.getProperty(EMWConfigConstants.EMW_CONFIG_BOOTSTRAP_JDBC_USERNAME);
        String password = environment.getProperty(EMWConfigConstants.EMW_CONFIG_BOOTSTRAP_JDBC_PASSWORD);
        String pollingInterval = environment.getProperty(EMWConfigConstants.EMW_CONFIG_BOOTSTRAP_POLLING_INTERVAL);
        String pollingDBLogSampleRate = environment.getProperty(EMWConfigConstants.EMW_CONFIG_BOOTSTRAP_POLLING_DB_LOG_RATE);
        String restPort = environment.getProperty(EMWConfigConstants.EMW_CONFIG_BOOTSTRAP_REST_PORT);

        EMWConfigParams params = EMWConfigParams.createFromOptionalStringParams(jdbcUrl, username, password, pollingInterval,
                pollingDBLogSampleRate, restPort);

        EMWConfigStarter.run(params);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}