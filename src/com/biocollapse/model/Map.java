package src.com.biocollapse.model;

import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import src.com.biocollapse.util.GlobalRandom;

public class Map {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private static ArrayList<File> mapFiles;

    private Block[][] map;

    public Map() {
        map = new Block[HEIGHT][WIDTH];
        initializeMap();
    }

    private static void getMapsFromFolder(File folder) {
        final File mapFolder = folder;
        final File[] mapFileArray = mapFolder.listFiles();
        if (mapFileArray.length > 0) {
            for (final File fileEntry : mapFolder.listFiles()) {
                mapFiles.add(fileEntry);
                System.out.println(fileEntry.getName());
            }
        }
    }

    private static void getMapsFromFolder() {
        final File mapFolder = new File("maps");
        if (!mapFolder.exists()) {
            mapFolder.mkdir();
        }
        getMapsFromFolder(mapFolder);
    }

    private void initializeMap() {
        Random random = GlobalRandom.getInstance();
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

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
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
        getMapsFromFolder();
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }
}
