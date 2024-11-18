package src.com.biocollapse.view;

import java.awt.Color;
import javax.swing.JPanel;
import src.com.biocollapse.model.Map;

public class MapPanel extends JPanel {

    /**
     * This panel is responsible for displaying the map.
     */
    public MapPanel() {
        setSize(Map.getWidth(), Map.getHeight());
        setBackground(Color.BLUE);
        // TODO: Implement layout.
    }

    /**
     * Called every frame to display the updated map.
     * @param map
     */
    public void update(Map map) {
        // TODO: Implement display method.
    }
}
