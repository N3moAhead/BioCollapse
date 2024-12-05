// Authors: Lukas, Johann, Sebastian 
package src.com.biocollapse.model;

import java.util.HashMap;

public enum Block {
    Grass(0xFF1B300A, 'g'),
    Path(0xFF71717A, 'p'),
    Hospital(0xFFE84EFB, 'H'),
    House(0xFFD97706, 'h'),
    Workplace(0xFF1E40AF, 'w'),;

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
