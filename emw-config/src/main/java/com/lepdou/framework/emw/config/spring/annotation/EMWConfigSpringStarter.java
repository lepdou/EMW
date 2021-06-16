/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lepdou.framework.emw.config.spring.annotation;

import com.lepdou.framework.emw.config.EMWConfigParams;
import com.lepdou.framework.emw.config.EMWConfigStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 *
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

        String jdbcUrl = attributes.getString("jdbcUrl");
        String username = attributes.getString("username");
        String password = attributes.getString("password");

        EMWConfigParams params = EMWConfigParams.builder().jdbcUrl(jdbcUrl).username(username).password(password).build();

        EMWConfigStarter.run(params);
    }

}