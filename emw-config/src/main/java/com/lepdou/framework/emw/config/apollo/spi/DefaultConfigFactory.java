package com.lepdou.framework.emw.config.apollo.spi;

import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.lepdou.framework.emw.config.core.DBConfigRepository;
import com.lepdou.framework.emw.config.apollo.Config;
import com.lepdou.framework.emw.config.apollo.ConfigFile;
import com.lepdou.framework.emw.config.apollo.internals.ConfigRepository;
import com.lepdou.framework.emw.config.apollo.internals.DefaultConfig;
import com.lepdou.framework.emw.config.apollo.internals.JsonConfigFile;
import com.lepdou.framework.emw.config.apollo.internals.PropertiesConfigFile;
import com.lepdou.framework.emw.config.apollo.internals.TxtConfigFile;
import com.lepdou.framework.emw.config.apollo.internals.XmlConfigFile;
import com.lepdou.framework.emw.config.apollo.internals.YamlConfigFile;
import com.lepdou.framework.emw.config.apollo.internals.YmlConfigFile;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class DefaultConfigFactory implements ConfigFactory {

    public DefaultConfigFactory() {
    }

    @Override
    public Config create(String namespace) {
        return new DefaultConfig(namespace, new DBConfigRepository(namespace));
    }

    @Override
    public ConfigFile createConfigFile(String namespace, ConfigFileFormat configFileFormat) {
        ConfigRepository configRepository = new DBConfigRepository(namespace);
        switch (configFileFormat) {
            case Properties:
                return new PropertiesConfigFile(namespace, configRepository);
            case XML:
                return new XmlConfigFile(namespace, configRepository);
            case JSON:
                return new JsonConfigFile(namespace, configRepository);
            case YAML:
                return new YamlConfigFile(namespace, configRepository);
            case YML:
                return new YmlConfigFile(namespace, configRepository);
            case TXT:
                return new TxtConfigFile(namespace, configRepository);
        }

        return null;
    }

}
