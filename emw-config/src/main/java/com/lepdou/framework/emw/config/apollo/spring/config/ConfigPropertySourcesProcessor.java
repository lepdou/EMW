package com.lepdou.framework.emw.config.apollo.spring.config;

import com.ctrip.framework.foundation.internals.ServiceBootstrap;
import com.lepdou.framework.emw.config.apollo.spring.spi.ConfigPropertySourcesProcessorHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * Apollo Property Sources processor for Spring XML Based Application
 *
 * @author Jason Song(song_s@ctrip.com)
 */
public class ConfigPropertySourcesProcessor extends PropertySourcesProcessor
        implements BeanDefinitionRegistryPostProcessor {

    private ConfigPropertySourcesProcessorHelper helper = ServiceBootstrap.loadPrimary(ConfigPropertySourcesProcessorHelper.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        helper.postProcessBeanDefinitionRegistry(registry);
    }
}
