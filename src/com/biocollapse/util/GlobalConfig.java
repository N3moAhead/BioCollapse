package src.com.biocollapse.util;

import src.com.biocollapse.model.Config;

/**
 * A global provider for the config of the simulation
 * It can be initialized via the
 * GlobalConfig.config.setConfig() function
 */
public class GlobalConfig {
  public static final Config config = new Config();

  private GlobalConfig() {
  }

  public static Config getConfig() {
    return config;
  }
}
