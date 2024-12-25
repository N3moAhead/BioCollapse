// Authors: Lukas, Sebastian
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
    return new Config(1, 5, 1, 20, 3, 15, 100, 10, 20, 25, 50, 25, false, false, false, false, "schwetzingen.bmp",
        1337);
  }

  /**
   * The current simulation config.
   */
  public static Config getConfig() {
    return config;
  }

  /**
   * Set the config.
   * 
   * @param config
   */
  public static void setConfig(Config config) {
    GlobalConfig.config = config;
  }
}
