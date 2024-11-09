package src.com.biocollapse.model;

import java.util.Random;

enum Block {
  Grass,
  Path,
  House,
  Hospital
}

public class Map {
  private static final int WIDTH = 100;
  private static final int HEIGHT = 100;
  private Block[][] map;

  public Map(long seed) {
    map = new Block[HEIGHT][WIDTH];
    initializeMap(seed);
  }
  public Map() {
    this((long) System.currentTimeMillis());
  }

  private void initializeMap(long seed) {
    Random random = new Random(seed);
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        map[y][x] = Block.values()[random.nextInt(Block.values().length)];
      }
    }
  }

  public void printMap() {
    for (int y = 0; y < HEIGHT; y++) {
      for (int x = 0; x < WIDTH; x++) {
        System.out.print(map[y][x].name().charAt(0) + " ");
      }
      System.out.println();
    }
  }

  public Block getBlock(int x, int y) {
    if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
      return map[y][x];
    }
    throw new IndexOutOfBoundsException("Invalid map coordinates");
  }

  public static void main(String[] args) {
    Map map = new Map();
    map.printMap();
  }
}
