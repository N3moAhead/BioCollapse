package src.com.biocollapse.model;

import java.awt.Color;
import java.util.EnumMap;

public class Colors {
    private static EnumMap<Block, Color> legend;
    static {
        legend = new EnumMap<>(Block.class);
        legend.put(Block.Grass,     new Color(216,255,192));
        legend.put(Block.Path,      new Color(194,201,201));
        legend.put(Block.Hospital,  new Color(255,198,198));
        legend.put(Block.House,     new Color(255,195,154));
        legend.put(Block.Workplace, new Color(133,206,209));
    }

    public static int getRGB(Block block) {
        return legend.get(block).getRGB();
    }
    public static Color get(Block block) {
        return legend.get(block);
    }
}
