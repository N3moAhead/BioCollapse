// Authors: Johann
package src.com.biocollapse.model;

import java.awt.Color;
import src.com.biocollapse.model.GraphItem.GraphType;

public class GraphItem {

    private final String name;
    private final int value;
    private final boolean visible;
    private final boolean alwaysHidden;
    private final Color color;
    private final GraphType type;

    public GraphItem(String name, int value, boolean visible, boolean alwaysHidden, Color color) {
        this.name = name;
        this.value = value;
        this.visible = visible;
        this.alwaysHidden = alwaysHidden;
        this.color = color;
        this.type = GraphType.DEFAULT;
    }

    public GraphItem(String name, int value, boolean visible, boolean alwaysHidden, Color color, GraphType type) {
        if (type == GraphType.PERCENTAGE) {
            name+=" %";
        }
        
        this.name = name;
        this.value = value;
        this.visible = visible;
        this.alwaysHidden = alwaysHidden;
        this.color = color;
        this.type = type;
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

    public enum GraphType {
        DEFAULT,
        PERCENTAGE
    }

    public GraphType getType() {
        return type;
    }
}
