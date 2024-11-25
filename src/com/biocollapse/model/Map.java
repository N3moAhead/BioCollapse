package src.com.biocollapse.model;

import java.util.HashMap;
import java.util.Set;
import java.io.File;
import java.util.Scanner;

public class Map {
    private static HashMap<String, File> mapList = new HashMap<String, File>();
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    private Block[][] map;

    static {
        initializeMapList();
    }

    public Map(String name) {
        
    }

    public Map() {
        this("211124.txt");
    }

    private static void initializeMapList() {
        final File mapFolder = new File("maps");
        if (!mapFolder.exists()) {
            mapFolder.mkdir();
        } else {
            final File[] mapFileArray = mapFolder.listFiles();
            if (mapFileArray.length > 0) {
                for (final File fileEntry : mapFolder.listFiles()) {
                    mapList.put(fileEntry.getName().substring(0,fileEntry.getName().length() - 4), fileEntry);
                    System.out.println("Found file :" + fileEntry.getName().substring(0,fileEntry.getName().length() - 4));
                }
            }
        }
    }

    public static Set<String> getMapNames() {
        initializeMapList();
        return mapList.keySet();
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
        
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}
