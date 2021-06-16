package com.lepdou.framework.emw.config.apollo.internals;

import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.lepdou.framework.emw.config.apollo.PropertiesCompatibleConfigFile;
import com.lepdou.framework.emw.config.apollo.build.ApolloInjector;
import com.lepdou.framework.emw.config.apollo.exceptions.ApolloConfigException;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.lepdou.framework.emw.config.apollo.util.ExceptionUtil;
import com.lepdou.framework.emw.config.apollo.util.yaml.YamlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class YamlConfigFile extends PlainTextConfigFile implements PropertiesCompatibleConfigFile {
    private static final Logger     logger = LoggerFactory.getLogger(com.lepdou.framework.emw.config.apollo.internals.YamlConfigFile.class);
    private volatile     Properties cachedProperties;

    public YamlConfigFile(String namespace, ConfigRepository configRepository) {
        super(namespace, configRepository);
        tryTransformToProperties();
    }

    @Override
    public ConfigFileFormat getConfigFileFormat() {
        return ConfigFileFormat.YAML;
    }

    @Override
    protected void update(Properties newProperties) {
        super.update(newProperties);
        tryTransformToProperties();
    }

    @Override
    public Properties asProperties() {
        if (cachedProperties == null) {
            transformToProperties();
        }
        return cachedProperties;
    }

    private boolean tryTransformToProperties() {
        try {
            transformToProperties();
            return true;
        } catch (Throwable ex) {
            Tracer.logEvent("ApolloConfigException", ExceptionUtil.getDetailMessage(ex));
            logger.warn("yaml to properties failed, reason: {}", ExceptionUtil.getDetailMessage(ex));
        }
        return false;
    }

    private synchronized void transformToProperties() {
        cachedProperties = toProperties();
    }

    private Properties toProperties() {
        if (!this.hasContent()) {
            return new Properties();
        }

        try {
            return ApolloInjector.getInstance(YamlParser.class).yamlToProperties(getContent());
        } catch (Throwable ex) {
            ApolloConfigException exception = new ApolloConfigException(
                    "Parse yaml file content failed for namespace: " + m_namespace, ex);
            Tracer.logError(exception);
            throw exception;
        }
    }
}
