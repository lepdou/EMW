package com.lepdou.framework.emw.config.apollo.spring.annotation;

import com.ctrip.framework.foundation.internals.ServiceBootstrap;
import com.lepdou.framework.emw.config.apollo.spring.spi.ApolloConfigRegistrarHelper;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class ApolloConfigRegistrar implements ImportBeanDefinitionRegistrar {

    private ApolloConfigRegistrarHelper helper = ServiceBootstrap.loadPrimary(ApolloConfigRegistrarHelper.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        helper.registerBeanDefinitions(importingClassMetadata, registry);
    }
}
