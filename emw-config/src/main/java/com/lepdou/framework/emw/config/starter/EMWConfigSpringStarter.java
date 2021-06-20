/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.starter;

import com.lepdou.framework.emw.config.spring.EMWConfigConstants;
import com.lepdou.framework.emw.config.spring.EnableEMWConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Spring 注解的方式初始化入口。
 *
 * <pre>
 *
 * @EnableApolloConfig(value = "application", order = 10)
 * @EnableEMWConfig(jdbcUrl = "jdbc:mysql://localhost:3306/emw", username = "root", password = "admin")
 * public class AppConfig {
 * }
 *
 * </pre>
 * @author lepdou
 * @version : EMWConfigSpringStarter.java, v 0.1 2021年06月16日 下午8:33 lepdou Exp $
 */
public class EMWConfigSpringStarter implements ImportBeanDefinitionRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(EMWConfigSpringStarter.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableEMWConfig.class.getName()));
        if (attributes == null) {
            return;
        }

        logger.info("Start to init emw-config module.");

        String jdbcUrl = attributes.getString(EMWConfigConstants.EMW_CONFIG_PARAM_JDBC_URL);
        String username = attributes.getString(EMWConfigConstants.EMW_CONFIG_PARAM_JDBC_USERNAME);
        String password = attributes.getString(EMWConfigConstants.EMW_CONFIG_PARAM_JDBC_PASSWORD);
        String pollingInterval = attributes.getString(EMWConfigConstants.EMW_CONFIG_PARAM_POLLING_INTERVAL);
        String pollingDBLogSampleRate = attributes.getString(EMWConfigConstants.EMW_CONFIG_PARAM_POLLING_DB_LOG_RATE);
        String restPort = attributes.getString(EMWConfigConstants.EMW_CONFIG_PARAM_REST_PORT);

        EMWConfigParams params = EMWConfigParams.createFromOptionalStringParams(jdbcUrl, username, password, pollingInterval,
                pollingDBLogSampleRate, restPort);

        EMWConfigStarter.run(params);
    }

}