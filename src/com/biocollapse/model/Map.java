package src.com.biocollapse.model;

import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import src.com.biocollapse.util.GlobalRandom;

public class Map {
    private static ArrayList<Map> mapFiles;

    private int width;
    private int height;
    private Block[][] map;

    public Map() {
        this.width = 100;
        this.height = 100;
        this.map = new Block[height][width];
        this.initializeRandomMap();
    }

    public Map(File file) {

    }

    private static void initialszeMapsFromFolder() {
        final File mapFolder = new File("maps");
        if (!mapFolder.exists()) {
            mapFolder.mkdir();
        } else {
            final File[] mapFileArray = mapFolder.listFiles();
            if (mapFileArray.length > 0) {
                for (final File fileEntry : mapFolder.listFiles()) {
                    mapFiles.add(new Map(fileEntry));
                    System.out.println(fileEntry.getName());
                }
            }
        }
    }

    private void initializeRandomMap() {
        Random random = GlobalRandom.getInstance();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = Block.values()[random.nextInt(Block.values().length)];
            }
        }
    }

    public void printMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(map[y][x].name().charAt(0) + " ");
            }
            System.out.println();
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
