// Authors: Lukas
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

  // Returns a random integer between the given min and max number
  public static int getRandIntBetween(int min, int max) {
    int n = instance.nextInt((max - min) + 1) + min;
    return n;
  }

  // Returns true if the given probability has occurred and 
  // returns false if the given probability has not occurred.
  public static boolean checkProbability(int probability) {
    if (getRandIntBetween(0, 100) <= probability) {
      return true;
    }
    return false;
  }
}
