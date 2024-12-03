// Authors: Lukas, Sebastian, Johann
package src.com.biocollapse.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Map {
    private static ArrayList<String> mapList = new ArrayList<String>();
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
    public Map(String fileName) {
        try {
            // Determine file extension
            int fileExtensionDot = fileName.lastIndexOf(".");
            String fileExtension = "";
            if (fileExtensionDot > 0) {
                fileExtension = fileName.substring(fileExtensionDot, fileName.length());
            } else {
                throw new IOException("Could not determine file Type of " + fileName);
            }

            // Use correct function for given file extension
            switch (fileExtension.toLowerCase()) {
                case ".txt":
                    parseTxtMap(fileName);
                    break;
                case ".bmp":
                    parseBmpMap(fileName);
                    break;
                default:
                    throw new IOException("File extension not supported: " + fileExtension);
            }
        } catch (IOException e) {
            System.out.println("File type error.");
            e.printStackTrace();
        }
    }

    private void parseTxtMap(String fileName) {
        try {
            this.map = new Block[MAP_HEIGHT][MAP_WIDTH];
            File mapFile = new File("maps/" + fileName);
            int row;
            int col;
            Scanner mapReader = new Scanner(mapFile);
            for (row = 0; mapReader.hasNextLine() && row < MAP_HEIGHT; row++) {
                String line = mapReader.nextLine();
                char[] chars = line.toCharArray();
                for (col = 0; col < chars.length && col < MAP_WIDTH; col++) {
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
                            this.map[row][col] = Block.Grass;
                            System.out.println("Invalid character '" + chars[col] + "' found at row: " + (row+1) + ", col: " + (col+1) + ". Set to Grass.");
                    }
                }
                if (col < MAP_WIDTH) {
                    System.out.println("Map too slim. Filling up empty spaces with Grass.");
                    for (;col < MAP_WIDTH;col++) {
                        this.map[row][col] = Block.Grass;
                    }
                }
            }
            if (row < MAP_HEIGHT) {
                System.out.println("Map too short. Filling up empty spaces with Grass.");
                for (;row < MAP_HEIGHT;row++) {
                    for (col = 0 ; col < MAP_WIDTH ; col++) {
                        this.map[row][col] = Block.Grass;
                    }
                }
            }
            mapReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: maps/" + fileName);
            e.printStackTrace();
        }
        
    }

    private void parseBmpMap(String fileName) {

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
        }
        final File[] mapFileArray = mapFolder.listFiles();
        if (mapFileArray.length > 0) {
            for (final File fileEntry : mapFolder.listFiles()) {
                mapList.add(fileEntry.getName());
                System.out.println("Found file: " + fileEntry.getName());
            }
        } else {
            System.out.println("Error: No maps found.");
        }
    }

    public static ArrayList<String> getMapNames() {
        return mapList;
    }

    public static String getSomeMapName() {
        if (mapList.isEmpty()) {
            throw new NoSuchElementException("List of maps is empty.");
        }
        return mapList.get(0);
    }

    public void printMap() {
        for (int row = 0; row < MAP_HEIGHT; row++) {
            for (int col = 0; col < MAP_WIDTH; col++) {
                System.out.print(map[row][col].name().charAt(0) + " ");
            }
            System.out.println();
        }
    }

    /**
     * A BFS Search Algorithm to find the nearest
     * Block of a certain type that a player
     * can walk to from a certain position.
     * 
     * @param target The Block we want to find
     * @param from   The position we start from
     * @return MapPosition if a block could be found and Null if the Block could not
     *         be found
     */
    public MapPosition findNearest(Block target, MapPosition from) {
        // Check if the starting point is a path
        Queue<MapPosition> queue = new LinkedList<>();
        boolean[][] visited = new boolean[MAP_HEIGHT][MAP_WIDTH];
        // Adding the starting point
        queue.add(from.copy());
        visited[from.getRow()][from.getCol()] = true;
        while (!queue.isEmpty()) {
            MapPosition current = queue.poll();
            // Target found?
            if (map[current.getRow()][current.getCol()] == target) {
                return current;
            }
            // Check neighbours
            for (MovementAction move : MovementAction.values()) {
                if (move == MovementAction.NONE)
                    continue;
                MapPosition newPos = current.getByMove(move);
                if (isValidPosition(newPos)) {
                    int newRow = newPos.getRow();
                    int newCol = newPos.getCol();
                    // Only check not visited positions
                    if (!visited[newRow][newCol]
                            && (map[newRow][newCol] == Block.Path || map[newRow][newCol] == target)) {
                        queue.add(newPos);
                        visited[newRow][newCol] = true;
                    }
                }
            }
        }
        // The target could not be found
        return null;
    }

    public Block[][] getMap() {
        return this.map;
    }

    private boolean isValidPosition(int row, int col) {
        return col >= 0 && col < MAP_WIDTH && row >= 0 && row < MAP_HEIGHT;
    }

    private boolean isValidPosition(MapPosition pos) {
        return isValidPosition(pos.getRow(), pos.getCol());
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

    // ! Just for debugging. Can be removed
    public static void main(String[] args) {
        Map test = new Map(getSomeMapName());
        test.printMap();
    }

}
