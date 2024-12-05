// Authors: Lukas, Sebastian, Johann
package src.com.biocollapse.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

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

    /**
     * Creates a Map based on a fileName.
     * The file extension is needed as this only supports files with .txt or .bmp
     * extension.
     * 
     * Valid filenames are provided by `getMapNames();`
     * 
     * @param fileName
     */
    public Map(String fileName) {
        // Initialize default map.
        this.map = new Block[MAP_HEIGHT][MAP_WIDTH];
        for (int row = 0 ; row < MAP_HEIGHT ; row++) {
            Arrays.fill(this.map[row], Block.Grass);
        }
        // Try to read a map from a file
        try {
            if (mapList.contains(fileName)) {
                // Determine file extension
                int fileExtensionDot = fileName.lastIndexOf(".");
                if (fileExtensionDot > 0) {
                    String fileExtension = fileName.substring(fileExtensionDot, fileName.length());

                    // Use correct function for given file extension
                    File mapFile = new File("maps/" + fileName);
                    switch (fileExtension.toLowerCase()) {
                        case ".txt":
                            Scanner mapReader = new Scanner(mapFile);
                            for (int row = 0; mapReader.hasNextLine() && row < MAP_HEIGHT; row++) {
                                char[] chars = mapReader.nextLine().toCharArray();
                                for (int col = 0; col < chars.length && col < MAP_WIDTH; col++) {
                                    this.map[row][col] = Block.fromChar(chars[col]);
                                }
                            }
                            mapReader.close();
                            break;
                        case ".bmp":
                            BufferedImage image = ImageIO.read(mapFile);
                            for (int row = 0; row < image.getWidth() && row < MAP_HEIGHT; row++) {
                                for (int col = 0; col < image.getHeight() && col < MAP_WIDTH; col++) {
                                    this.map[row][col] = Block.fromColor(image.getRGB(row, col));
                                }
                            }
                            break;
                        default:
                            throw new IOException("File extension '" + fileExtension + "' is not supported.");
                    }
                } else {
                    throw new IOException("Could not determine file type.");
                }
            } else {
                throw new FileNotFoundException("File is not valid: " + fileName);
            }
        } catch (FileNotFoundException e) {
            updateMapList();
            System.out.println("Error: File not found.");
            System.out.print("Valid map files are: ");
            for (String mapName : mapList) {
                System.out.print("'" + mapName + "', ");
            }
            System.out.println();
            e.printStackTrace();

        } catch (IOException e) {
            System.out.println("Error while reading map: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Chooses the first map from the maps folder to be used.
     */
    public Map() {
        this(getSomeMapName());
    }

    /**
     * Update the list of valid map files from the map folder.
     */
    public static void updateMapList() {
        // Reset list to make sure old names are deleted
        mapList.clear();
        try {
            final File mapFolder = new File("maps");
            if (mapFolder.exists()) {
                final File[] mapFileArray = mapFolder.listFiles();
                if (mapFileArray.length > 0) {
                    for (int i = 0; i < mapFileArray.length; i++) {
                        mapList.add(mapFileArray[i].getName());
                    }
                    mapList.sort(null);
                } else {
                    throw new FileNotFoundException("Error: No maps found.");
                }
            } else {
                throw new FileNotFoundException("Error: 'maps' folder doesn't exist.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error while updating list of valid map names.");
            e.printStackTrace();
        }
    }

    /**
     * Get a list of map names.
     * 
     * Map names are sorted alphabetically.
     * 
     * @return an Arraylist of valid file names.
     */
    public static ArrayList<String> getMapNames() {
        return mapList;
    }

    /**
     * Get the first map name.
     * 
     * @return a valid map name
     */
    public static String getSomeMapName() {
        if (mapList.isEmpty()) {
            throw new NoSuchElementException("List of maps is empty.");
        }
        return mapList.get(0);
    }

    /**
     * Function for printing the map in the terminal.
     * 
     * Used for Debugging purposes only.
     * 
     * Note: there is a problem, where Hospitals and Homes lookthe same.
     */
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
                    Block newBlock = getBlock(newPos);
                    // Only check not visited positions
                    if (!visited[newRow][newCol]
                            && (newBlock == Block.Path || newBlock == target)) {
                        queue.add(newPos);
                        visited[newRow][newCol] = true;
                    }
                }
            }
        }
        // The target could not be found
        return null;
    }

    /**
     * Get a 2d Array to determine best path between two points.
     * 
     * @param from MapPosition
     * @param to   MapPosition
     * @return
     */
    public Integer[][] getStepMatrix(MapPosition from, MapPosition to) {
        Queue<MapPosition> queue = new LinkedList<>();
        Integer[][] stepMatrix = new Integer[MAP_HEIGHT][MAP_WIDTH];
        int step = 0;
        // Adding the starting point
        queue.add(from.copy());
        stepMatrix[from.getRow()][from.getCol()] = step++;
        while (!queue.isEmpty()) {
            MapPosition current = queue.poll();
            // Target found?
            if (current.equals(to)) {
                return stepMatrix;
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
                    if (stepMatrix[newRow][newCol] == null
                            && (map[newRow][newCol] == Block.Path || newPos.equals(to))) {
                        queue.add(newPos);
                        stepMatrix[newRow][newCol] = step;
                    }
                }
            }
            step++;
        }
        // The target could not be found
        return null;
    }

    public Block[][] getMap() {
        return this.map;
    }

    public static boolean isValidPosition(int row, int col) {
        return col >= 0 && col < MAP_WIDTH && row >= 0 && row < MAP_HEIGHT;
    }

    public static boolean isValidPosition(MapPosition pos) {
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
}
