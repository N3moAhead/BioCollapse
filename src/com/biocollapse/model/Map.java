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

    /*
     * Automatically load all map Files.
     * 
     * to update the list of files manually, it is also possible to call `Map.updateMapList()`.
     */
    static {
        updateMapList();
    }

    /*
     * Creates a Map based on a name.
     * The file extension .txt is not needed as this references the HashMap of all "loaded" files.
     * 
     * Valid filenames/keys are provided by getMapNames();
     */
    public Map(String name) {
        try {
            this.map = new Block[HEIGHT][WIDTH];
            File mapFile = mapList.get(name);
            Scanner mapReader = new Scanner(mapFile);
            for (int height = 0; mapReader.hasNextLine() && height < HEIGHT ; height++) {
                String line = mapReader.nextLine();
                char[] chars = line.toCharArray();
                for (int width = 0 ; width < chars.length && width < WIDTH ; width++) {
                    if (isValidPosition(height, width)) {
                        switch (chars[width]) {
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
                                System.out.println("Invalid character found: " + chars[width]);
                                break;
                        }
                    } else {
                        System.out.printf("Invalid Position: height=%i, width=%i.\n",height,width);
                    }
                }
            }
            mapReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not Found: maps/" + name + ".txt");
            e.printStackTrace();
        }
    }

    /*
     * Chooses one map File from the maps folder to be used
     * 
     * As the file list is a set there is no first map and as such this might give different results if not specified.
     */
    public Map() {
        this(getSomeMapName());
    }

    public static void updateMapList() {
        mapList.clear();
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
        String someMapName = "";
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
        map.printMap();
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}