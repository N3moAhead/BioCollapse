// Authors: Johann
package src.com.biocollapse.model;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import src.com.biocollapse.model.GraphItem.GraphType;

public class Graph extends ArrayList<GraphItem> {

    private boolean visible;
    private boolean alwaysHidden;
    private Color color;
    private String name;
    private int highestValue;
    private GraphType type;
    private final JLabel nameLabel = new JLabel();
    private final JLabel valueLabel = new JLabel("0");

    public void addGraphItem(GraphItem item) {
        if (item.getValue() > highestValue) {
            highestValue = item.getValue();
        }
        super.add(item);
        if (item.getType() == GraphType.PERCENTAGE) {
            valueLabel.setText(String.valueOf(item.getValue())+"%");
        } else {
            valueLabel.setText(String.valueOf(item.getValue()));
        }
        valueLabel.revalidate();
        valueLabel.repaint();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAlwaysHidden() {
        return alwaysHidden;
    }

    public void setAlwaysHidden(boolean alwaysHidden) {
        this.alwaysHidden = alwaysHidden;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nameLabel.setText(name);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
    }

    public int getHighestValue() {
        return highestValue;
    }

    public void setHighestValue(int highestValue) {
        this.highestValue = highestValue;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getValueLabel() {
        return valueLabel;
    }

    public GraphType getType() {
        return type;
    }

    public void setType(GraphType type) {
        this.type = type;
    }
}
