package com.lepdou.framework.emw.config.apollo.internals;

import com.google.common.base.Preconditions;
import com.lepdou.framework.emw.config.apollo.ConfigFileChangeListener;
import com.lepdou.framework.emw.config.apollo.PropertiesCompatibleConfigFile;
import com.lepdou.framework.emw.config.apollo.enums.ConfigSourceType;
import com.lepdou.framework.emw.config.apollo.model.ConfigFileChangeEvent;

import java.util.Properties;

public class PropertiesCompatibleFileConfigRepository extends AbstractConfigRepository implements
        ConfigFileChangeListener {
    private final    PropertiesCompatibleConfigFile configFile;
    private volatile Properties                     cachedProperties;

    public PropertiesCompatibleFileConfigRepository(PropertiesCompatibleConfigFile configFile) {
        this.configFile = configFile;
        this.configFile.addChangeListener(this);
        this.trySync();
    }

    @Override
    protected synchronized void sync() {
        Properties current = configFile.asProperties();

        Preconditions.checkState(current != null, "PropertiesCompatibleConfigFile.asProperties should never return null");

        if (cachedProperties != current) {
            cachedProperties = current;
            this.fireRepositoryChange(configFile.getNamespace(), cachedProperties);
        }
    }

    @Override
    public Properties getConfig() {
        if (cachedProperties == null) {
            sync();
        }
        return cachedProperties;
    }

    @Override
    public void setUpstreamRepository(ConfigRepository upstreamConfigRepository) {
        //config file is the upstream, so no need to set up extra upstream
    }

    @Override
    public ConfigSourceType getSourceType() {
        return configFile.getSourceType();
    }

    @Override
    public void onChange(ConfigFileChangeEvent changeEvent) {
        this.trySync();
    }
}
