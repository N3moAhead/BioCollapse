package src.com.biocollapse.model;

import java.awt.Color;

public class GraphItem {

    private final String name;
    private final int value;
    private final boolean visible;
    private final boolean alwaysHidden;
    private final Color color;

    public GraphItem(String name, int value, boolean visible, boolean alwaysHidden, Color color) {
        this.name = name;
        this.value = value;
        this.visible = visible;
        this.alwaysHidden = alwaysHidden;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isAlwaysHidden() {
        return alwaysHidden;
    }

    public Color getColor() {
        return color;
    }

}
