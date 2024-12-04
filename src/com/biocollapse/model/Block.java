// Authors: Lukas, Johann
package src.com.biocollapse.model;

import java.util.HashMap;

public enum Block {
    Grass(0xFFD8FFC0, 'g'),
    Path(0xFFC2C9C9, 'p'),
    Hospital(0xFFFFC6C6, 'H'),
    House(0xFFFFC39A, 'h'),
    Workplace(0xFF85CED1, 'w'),;

    private final int argb;
    private final char character;

    // Constructor
    Block(int argb, char character) {
        this.argb = argb;
        this.character = character;
    }

    // Getter
    public int getArgb() {
        return argb;
    }

    public char getCharacter() {
        return character;
    }

    // Centralized mapping
    private static final HashMap<Integer, Block> COLOR_MAP = new HashMap<>();
    private static final HashMap<Character, Block> CHAR_MAP = new HashMap<>();

    // Static block to populate the map
    static {
        for (Block block : Block.values()) {
            COLOR_MAP.put(block.argb, block);
            CHAR_MAP.put(block.character, block);
        }
    }

    // Methods to get Block from color or character
    public static Block fromColor(int color) {
        Block block = COLOR_MAP.get(color);
        if (block == null) {
            System.out.println("Unknown color: " + Integer.toHexString(color) + ". Set to Grass.");
            block = Block.Grass;
        }
        return block;
    }

    public static Block fromChar(char character) {
        Block block = CHAR_MAP.get(character);
        if (block == null) {
            System.out.println("Unknown character: " + character + ". Set to Grass.");
            block = Block.Grass;
        }
        return block;
    }
}
