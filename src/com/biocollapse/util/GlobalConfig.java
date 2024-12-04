// Authors: Lukas
package src.com.biocollapse.util;

import src.com.biocollapse.model.Config;

/**
 * A global provider for the config of the simulation
 * It can be initialized via the
 * GlobalConfig.config.setConfig() function
 */
public class GlobalConfig {
  public static Config config = defaultConfig();

  private GlobalConfig() {
  }

  /**
   * Reset the config to it's default values.
   */
  public static void resetToDefault() {
    config = defaultConfig();
  }

  /**
   * The default values of the simulation.
   */
  private static Config defaultConfig() {
    return new Config(6, 50, 3, 3, 14, 90, 300, 50, 80, 25, 50, 25, false, false, false, false);
  }

  /**
   * The current simulation config.
   */
  public static Config getConfig() {
    return config;
  }
}
