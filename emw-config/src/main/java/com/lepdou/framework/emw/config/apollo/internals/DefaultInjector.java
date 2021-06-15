package com.lepdou.framework.emw.config.apollo.internals;

import com.ctrip.framework.apollo.tracer.Tracer;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Singleton;
import com.lepdou.framework.emw.config.apollo.exceptions.ApolloConfigException;
import com.lepdou.framework.emw.config.apollo.spi.ConfigFactory;
import com.lepdou.framework.emw.config.apollo.spi.ConfigFactoryManager;
import com.lepdou.framework.emw.config.apollo.spi.ConfigRegistry;
import com.lepdou.framework.emw.config.apollo.spi.DefaultConfigFactory;
import com.lepdou.framework.emw.config.apollo.spi.DefaultConfigFactoryManager;
import com.lepdou.framework.emw.config.apollo.spi.DefaultConfigRegistry;
import com.lepdou.framework.emw.config.apollo.util.ConfigUtil;
import com.lepdou.framework.emw.config.apollo.util.http.HttpUtil;
import com.lepdou.framework.emw.config.apollo.util.yaml.YamlParser;

/**
 * Guice injector
 * @author Jason Song(song_s@ctrip.com)
 */
public class DefaultInjector implements Injector {
  private com.google.inject.Injector m_injector;

  public DefaultInjector() {
    try {
      m_injector = Guice.createInjector(new ApolloModule());
    } catch (Throwable ex) {
      ApolloConfigException exception = new ApolloConfigException("Unable to initialize Guice Injector!", ex);
      Tracer.logError(exception);
      throw exception;
    }
  }

  @Override
  public <T> T getInstance(Class<T> clazz) {
    try {
      return m_injector.getInstance(clazz);
    } catch (Throwable ex) {
      Tracer.logError(ex);
      throw new ApolloConfigException(
          String.format("Unable to load instance for %s!", clazz.getName()), ex);
    }
  }

  @Override
  public <T> T getInstance(Class<T> clazz, String name) {
    //Guice does not support get instance by type and name
    return null;
  }

  private static class ApolloModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(ConfigManager.class).to(DefaultConfigManager.class).in(Singleton.class);
      bind(ConfigFactoryManager.class).to(DefaultConfigFactoryManager.class).in(Singleton.class);
      bind(ConfigRegistry.class).to(DefaultConfigRegistry.class).in(Singleton.class);
      bind(ConfigFactory.class).to(DefaultConfigFactory.class).in(Singleton.class);
      bind(ConfigUtil.class).in(Singleton.class);
      bind(HttpUtil.class).in(Singleton.class);
      bind(ConfigServiceLocator.class).in(Singleton.class);
      bind(RemoteConfigLongPollService.class).in(Singleton.class);
      bind(YamlParser.class).in(Singleton.class);
    }
  }
}
