package src.com.biocollapse.model;

import java.util.HashMap;
import java.util.Set;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map {
    private static HashMap<String, File> mapList = new HashMap<String, File>();
    public static final int MAP_WIDTH = 100;
    public static final int MAP_HEIGHT = 100;

    private Block[][] map;

    /*
     * Automatically load all map Files.
     * 
     * to update the list of files manually, it is also possible to call
     * `Map.updateMapList()`.
     */
    static {
        updateMapList();
    }

    /*
     * Creates a Map based on a name.
     * The file extension .txt is not needed as this references the HashMap of all
     * "loaded" files.
     * 
     * Valid filenames/keys are provided by getMapNames();
     */
    public Map(String name) {
        try {
            this.map = new Block[MAP_HEIGHT][MAP_WIDTH];
            File mapFile = mapList.get(name);
            Scanner mapReader = new Scanner(mapFile);
            for (int row = 0; mapReader.hasNextLine() && row < MAP_HEIGHT; row++) {
                String line = mapReader.nextLine();
                char[] chars = line.toCharArray();
                for (int col = 0; col < chars.length && col < MAP_WIDTH; col++) {
                    if (isValidPosition(row, col)) {
                        switch (chars[col]) {
                            case 'g':
                                this.map[row][col] = Block.Grass;
                                break;
                            case 'p':
                                this.map[row][col] = Block.Path;
                                break;
                            case 'h':
                                this.map[row][col] = Block.House;
                                break;
                            case 'H':
                                this.map[row][col] = Block.Hospital;
                                break;
                            case 'w':
                                this.map[row][col] = Block.Workplace;
                                break;

                            default:
                                System.out.println("Invalid character found: " + chars[col]);
                                break;
                        }
                    } else {
                        System.out.printf("Invalid Position: row=%i, col=%i.\n", row, col);
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
     * As the file list is a set there is no first map and as such this might give
     * different results if not specified.
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
                    mapList.put(fileEntry.getName().substring(0, fileEntry.getName().length() - 4), fileEntry);
                    System.out.println(
                            "Found file :" + fileEntry.getName().substring(0, fileEntry.getName().length() - 4));
                }
            }
        }
    }

    public static Set<String> getMapNames() {
        return mapList.keySet();
    }

    public static String getSomeMapName() {
        String someMapName = "";
        for (String mapName : getMapNames()) {
            someMapName = mapName;
            break;
        }
        return someMapName;
    }

    public void printMap() {
        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                System.out.print(map[row][col].name().charAt(0) + " ");
            }
            System.out.println();
        }
    }

    private boolean isValidPosition(int row, int col) {
        return col >= 0 && col < MAP_WIDTH && row >= 0 && row < MAP_HEIGHT;
    }

    public Block getBlock(int col, int row) {
        if (isValidPosition(col, row)) {
            return map[row][col];
        }
        throw new IndexOutOfBoundsException("Invalid map coordinates");
    }

    public Block getBlock(MapPosition pos) {
        return getBlock(pos.getCol(), pos.getRow());
    }

    //! Just for debugging. Can be removed
    public static void main(String[] args) {
        Map test = new Map(getSomeMapName());
        test.printMap();
    }

}
