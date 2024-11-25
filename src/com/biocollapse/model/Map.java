package src.com.biocollapse.model;

import java.util.HashMap;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
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
        try {
            File mapFile = mapList.get(name);
            Scanner mapReader = new Scanner(mapFile);
            int height = 0;
            int width = 0;
            while (mapReader.hasNextLine()) {
                String line = mapReader.nextLine();
                for (char block : line.toCharArray()) {
                    switch (block) {
                        case 'g':
                            this.map[height][width] = Block.Grass;
                            break;
                        case 'p':
                            this.map[height][width] = Block.Path;
                            break;
                        case 'h':
                            this.map[height][width] = Block.House;
                            break;
                        case 'H':
                            this.map[height][width] = Block.Hospital;
                            break;
                        case 'w':
                            this.map[height][width] = Block.Workplace;
                            break;
                    
                        default:
                            System.out.println("Invalid character found: " + block);
                            break;
                    }
                    width++;
                }
                height++;
            }
            mapReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not Found: maps/" + name + ".txt");
            e.printStackTrace();
        }
    }

    public Map() {
        this(getSomeMapName());
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
        return mapList.keySet();
    }

    public static String getSomeMapName() {
        String someMapName = "211124.txt";
        for(String mapName: getMapNames()) {
            someMapName = mapName;
            break;
        }
        return someMapName;
    }

    public void printMap() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                System.out.print(map[y][x].name().charAt(0) + " ");
            }
            System.out.println();
        }
    }

    public boolean isValidPosition(int x, int y) {
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
        
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}

class InvalidCharacterException extends Exception {
    public InvalidCharacterException(String message) {
        super(message);
    }
}