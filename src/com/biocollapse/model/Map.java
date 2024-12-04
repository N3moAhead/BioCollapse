// Authors: Lukas, Sebastian, Johann
package src.com.biocollapse.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import src.com.biocollapse.model.Colors;

public class Map {
    private static ArrayList<String> mapList = new ArrayList<String>();
    public static final int MAP_WIDTH = 100;
    public static final int MAP_HEIGHT = 100;

    private static final int pathCol = new Color(0, 0, 0).getRGB();

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
                    this.map = parseTxtMap(fileName);
                    break;
                case ".bmp":
                    this.map = parseBmpMap(fileName);
                    break;
                default:
                    throw new IOException("File extension not supported: " + fileExtension);
            }
        } catch (FileNotFoundException e) {
            for (int row = 0; row < MAP_HEIGHT; row++) {
                for (int col = 0; col < MAP_WIDTH; col++) {
                    this.map[row][col] = Block.Grass;
                }
            }
            System.out.println("File not found: maps/" + fileName);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File error.");
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
     * Parses a .txt file into a Block[][]
     * 
     * @param fileName
     * @return a map as Block[][]
     */
    private static Block[][] parseTxtMap(String fileName) throws FileNotFoundException {
        Block[][] map = new Block[MAP_HEIGHT][MAP_WIDTH];
        // Load file.
        File mapFile = new File("maps/" + fileName);
        int row;
        int col;
        // Scan text.
        Scanner mapReader = new Scanner(mapFile);
        for (row = 0; mapReader.hasNextLine() && row < MAP_HEIGHT; row++) {
            String line = mapReader.nextLine();
            char[] chars = line.toCharArray();
            for (col = 0; col < chars.length && col < MAP_WIDTH; col++) {
                switch (chars[col]) {
                    case 'g':
                        map[row][col] = Block.Grass;
                        break;
                    case 'p':
                        map[row][col] = Block.Path;
                        break;
                    case 'h':
                        map[row][col] = Block.House;
                        break;
                    case 'H':
                        map[row][col] = Block.Hospital;
                        break;
                    case 'w':
                        map[row][col] = Block.Workplace;
                        break;

                    default:
                        map[row][col] = Block.Grass;
                        System.out.println("Invalid character '" + chars[col] + "' found at row: " + (row + 1)
                                + ", col: " + (col + 1) + ". Set to Grass.");
                }
            }
            if (col < MAP_WIDTH) {
                System.out.println("Map too slim. Filling up empty spaces with Grass.");
                for (; col < MAP_WIDTH; col++) {
                    map[row][col] = Block.Grass;
                }
            }
        }
        if (row < MAP_HEIGHT) {
            System.out.println("Map too short. Filling up empty spaces with Grass.");
            for (; row < MAP_HEIGHT; row++) {
                for (col = 0; col < MAP_WIDTH; col++) {
                    map[row][col] = Block.Grass;
                }
            }
        }
        mapReader.close();
        return map;
    }

    /**
     * Parses a .bmp file into a Block[][]
     * 
     * @param fileName
     * @return a map as Block[][]
     */
    private static Block[][] parseBmpMap(String fileName) throws IOException {
        Block[][] map = new Block[MAP_HEIGHT][MAP_WIDTH];
        File mapFile = new File("maps/" + fileName);
        BufferedImage image = ImageIO.read(mapFile);
        int row;
        int col;
        for (row = 0; row < image.getWidth() && row < MAP_HEIGHT; row++) {
            for (col = 0; col < image.getHeight() && col < MAP_WIDTH; col++) {
                // These are if else statements because switch case requires "constants" for the cases...
                if (image.getRGB(row, col) == Colors.getRGB(Block.Grass)) {
                    map[row][col] = Block.Grass;
                } else if (image.getRGB(row, col) == Colors.getRGB(Block.Path)) {
                    map[row][col] = Block.Path;
                } else if (image.getRGB(row, col) == Colors.getRGB(Block.Hospital)) {
                    map[row][col] = Block.Hospital;
                } else if (image.getRGB(row, col) == Colors.getRGB(Block.House)) {
                    map[row][col] = Block.House;
                } else if (image.getRGB(row, col) == Colors.getRGB(Block.Workplace)) {
                    map[row][col] = Block.Workplace;
                } else {
                    map[row][col] = Block.Grass;
                    System.out.println("Invalid Color at row: " + row + ", col: " + col + ". Set to Grass");
                }
            }
            if (col < MAP_WIDTH) {
                System.out.println("Map too slim. Filling up empty spaces with Grass.");
                for (; col < MAP_WIDTH; col++) {
                    map[row][col] = Block.Grass;
                }
            }
        }
        if (row < MAP_HEIGHT) {
            System.out.println("Map too short. Filling up empty spaces with Grass.");
            for (; row < MAP_HEIGHT; row++) {
                for (col = 0; col < MAP_WIDTH; col++) {
                    map[row][col] = Block.Grass;
                }
            }
        }
        return map;

    }

    /**
     * Update the list of valid map files from the map folder.
     * 
     * Creates a map folfer if not present.
     */
    public static void updateMapList() {
        // Reset list to make sure old names are deleted
        mapList.clear();
        final File mapFolder = new File("maps");
        // Create the folder if it doesn't exist.
        if (!mapFolder.exists()) {
            mapFolder.mkdir();
        }
        final File[] mapFileArray = mapFolder.listFiles();
        if (mapFileArray.length > 0) {
            for (int i = 0; i < mapFileArray.length; i++) {
                mapList.add(mapFileArray[i].getName());
                // Ouput the filenames for debugging purposes.
                System.out.println("Found file: " + mapFileArray[i].getName());
            }
            mapList.sort(null);
        } else {
            System.out.println("Error: No maps found.");
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
     * @return a map name
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

    // ! Just for debugging. Can be removed
    public static void main(String[] args) {
        Map test = new Map("city.bmp");
        test.printMap();
    }

}
