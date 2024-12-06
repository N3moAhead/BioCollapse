// Authors: Lukas, Johann, Sebastian 
package src.com.biocollapse.model;

import java.util.HashMap;

public enum Block {
    Grass(0xFF1B300A, 'g',"Gras"),
    Path(0xFF71717A, 'p',"Weg"),
    Hospital(0xFFE84EFB, 'H',"Krankenhaus"),
    House(0xFFD97706, 'h',"Haus"),
    Workplace(0xFF1E40AF, 'w',"Arbeitsplatz"),;

    // Centralized mapping
    private static final HashMap<Integer, Block> COLOR_MAP = new HashMap<>();
    private static final HashMap<Character, Block> CHAR_MAP = new HashMap<>();

    private final int argb;
    private final char character;
    private final String title;

    Block(int argb, char character, String title) {
        this.argb = argb;
        this.character = character;
        this.title = title;
    }

    /**
     * Populate the maps.
     */
    static {
        for (Block block : Block.values()) {
            COLOR_MAP.put(block.argb, block);
            CHAR_MAP.put(block.character, block);
        }
    }

    /**
     * Get the block from color.
     */
    public static Block fromColor(int color) {
        Block block = COLOR_MAP.get(color);
        if (block == null) {
            System.out.println("Unknown color: " + Integer.toHexString(color) + ". Set to Grass.");
            block = Block.Grass;
        }
        return block;
    }

    /**
     * Get the block from character.
     */
    public static Block fromChar(char character) {
        Block block = CHAR_MAP.get(character);
        if (block == null) {
            System.out.println("Unknown character: " + character + ". Set to Grass.");
            block = Block.Grass;
        }
        return block;
    }

    public int getArgb() {
        return argb;
    }

    public char getCharacter() {
        return character;
    }

    public String getTitle() {
        return title;
    }
}
