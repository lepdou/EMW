package com.lepdou.framework.emw.config.apollo;

import com.lepdou.framework.emw.config.apollo.model.ConfigFileChangeEvent;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public interface ConfigFileChangeListener {
  /**
   * Invoked when there is any config change for the namespace.
   * @param changeEvent the event for this change
   */
  void onChange(ConfigFileChangeEvent changeEvent);
}
