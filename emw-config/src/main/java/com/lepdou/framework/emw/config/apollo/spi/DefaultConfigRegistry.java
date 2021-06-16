package com.lepdou.framework.emw.config.apollo.spi;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class DefaultConfigRegistry implements ConfigRegistry {
    private static final Logger                     s_logger    = LoggerFactory.getLogger(
            com.lepdou.framework.emw.config.apollo.spi.DefaultConfigRegistry.class);
    private              Map<String, ConfigFactory> m_instances = Maps.newConcurrentMap();

    @Override
    public void register(String namespace, ConfigFactory factory) {
        if (m_instances.containsKey(namespace)) {
            s_logger.warn("ConfigFactory({}) is overridden by {}!", namespace, factory.getClass());
        }

        m_instances.put(namespace, factory);
    }

    @Override
    public ConfigFactory getFactory(String namespace) {
        ConfigFactory config = m_instances.get(namespace);

        return config;
    }
}
