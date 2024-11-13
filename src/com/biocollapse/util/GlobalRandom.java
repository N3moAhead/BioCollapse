package src.com.biocollapse.util;

import java.util.Random;

public class GlobalRandom {
  private static final Random instance = new Random();

  private GlobalRandom() {
  }

  public static void setSeed(long seed) {
    instance.setSeed(seed);
  }

  public static Random getInstance() {
    return instance;
  }
}
