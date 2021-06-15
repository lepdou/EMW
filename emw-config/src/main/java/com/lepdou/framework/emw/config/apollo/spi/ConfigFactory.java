package com.lepdou.framework.emw.config.apollo.spi;

import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.lepdou.framework.emw.config.apollo.Config;
import com.lepdou.framework.emw.config.apollo.ConfigFile;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public interface ConfigFactory {
  /**
   * Create the config instance for the namespace.
   *
   * @param namespace the namespace
   * @return the newly created config instance
   */
  public Config create(String namespace);

  /**
   * Create the config file instance for the namespace
   * @param namespace the namespace
   * @return the newly created config file instance
   */
  public ConfigFile createConfigFile(String namespace, ConfigFileFormat configFileFormat);
}
