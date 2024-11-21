package src.com.biocollapse.model;

import java.util.Random;
import src.com.biocollapse.util.GlobalRandom;

public class Map {
  public static final int MAP_WIDTH = 100;
  public static final int MAP_HEIGHT = 100;
  private Block[][] map;

  public Map() {
    map = new Block[MAP_HEIGHT][MAP_WIDTH];
    initializeMap();
  }

  private void initializeMap() {
    Random random = GlobalRandom.getInstance();
    for (int y = 0; y < MAP_HEIGHT; y++) {
      for (int x = 0; x < MAP_WIDTH; x++) {
        map[y][x] = Block.values()[random.nextInt(Block.values().length)];
      }
    }
  }

  public void printMap() {
    for (int y = 0; y < MAP_HEIGHT; y++) {
      for (int x = 0; x < MAP_WIDTH; x++) {
        System.out.print(map[y][x].name().charAt(0) + " ");
      }
      System.out.println();
    }
  }

  public Block[][] getMap() {
    return map;
  }

  private boolean isValidPosition(int x, int y) {
    return x >= 0 && x < MAP_WIDTH && y >= 0 && y < MAP_HEIGHT;
  }

  public Block getBlock(int x, int y) {
    if (isValidPosition(x, y)) {
      return map[y][x];
    }
    throw new IndexOutOfBoundsException("Invalid map coordinates");
  }

  public Block getBlock(MapPosition pos) {
    return getBlock(pos.getCol(), pos.getRow());
  }

  public static void main(String[] args) {
    Map map = new Map();
    map.printMap();
  }

  public static int getMAP_WIDTH() {
    return MAP_WIDTH;
  }

  public static int getMAP_HEIGHT() {
    return MAP_HEIGHT;
  }
}
