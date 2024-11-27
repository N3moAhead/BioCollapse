package src.com.biocollapse.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import src.com.biocollapse.controller.WindowController;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.LiveStatistics;

public class SimulationPanel extends JPanel {

    private final WindowController controller;
    private MapPanel map;
    private LiveStatisticsPanel stats;

    public SimulationPanel(WindowController controller) {
        this.controller = controller;
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(201, 218, 234));
        topBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 0));
        JLabel title = new JLabel("SIMULATION");
        topBar.add(title, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        JPanel centerContent = new JPanel(new BorderLayout());
        add(centerContent, BorderLayout.CENTER);

        JPanel innerMapPanel = new JPanel(new BorderLayout());
        centerContent.add(innerMapPanel, BorderLayout.WEST);
        map = new MapPanel();
        innerMapPanel.add(map, BorderLayout.CENTER);
        innerMapPanel.add(map.legendPanel(), BorderLayout.NORTH);

        stats = new LiveStatisticsPanel();
        centerContent.add(stats, BorderLayout.CENTER);
    }

    /**
     * Call each frame to update the human position and statistics.
     */
    public void update(List<Human> humanData, LiveStatistics liveStatistics) {
        map.update(humanData);
        stats.update(liveStatistics);
    }

    /**
     * Set the map layout.
     */
    public void setMap(Block[][] mapData) {
        map.setMap(mapData);
    }

    /**
     * Call when the simulation ended to display the statistics.
     */
    public void simulationComplete() {
        controller.showStatisticsScreen(stats.getTimelineStats());
    }

    /**
     * Map debugging. TODO: Remove later.
     *
     * @param args
     */
    public static void main(String[] args) {
        MapPanel.DEBUG_MAP = true;
        JFrame frame = new JFrame();
        Dimension d = MapPanel.getMapDimension();
        Dimension newDim = new Dimension((int) d.getWidth() + 460, (int) d.getHeight() + 475);
        frame.setSize(newDim);
        frame.setMinimumSize(newDim);
        frame.setPreferredSize(newDim);
        frame.setVisible(true);
        SimulationPanel panel = new SimulationPanel(null);
        frame.add(panel);
        panel.stats.update(new LiveStatistics(0,0,0,0,0,0));
        panel.stats.update(new LiveStatistics(1,1,1,1,1,1));
        panel.stats.update(new LiveStatistics(5,2,3,4,5,6));
        panel.stats.update(new LiveStatistics(3,3,3,3,3,3));
        panel.stats.update(new LiveStatistics(2,1,1,1,1,1));
        panel.stats.update(new LiveStatistics(1,1,1,1,1,1));
        panel.stats.update(new LiveStatistics(5,2,3,4,5,0));
        panel.stats.update(new LiveStatistics(3,3,3,3,3,3));
        panel.stats.update(new LiveStatistics(2,1,1,1,1,1));

        panel.stats.update(new LiveStatistics(5,2,3,4,5,6));
        panel.stats.update(new LiveStatistics(3,3,3,3,3,3));
        panel.stats.update(new LiveStatistics(2,1,1,1,1,1));
        panel.stats.update(new LiveStatistics(1,1,1,1,1,1));
        panel.stats.update(new LiveStatistics(5,2,3,4,5,0));
        panel.stats.update(new LiveStatistics(3,3,3,3,3,3));
        panel.stats.update(new LiveStatistics(2,1,1,1,1,1));

        panel.stats.update(new LiveStatistics(0,0,0,0,0,0));
        panel.stats.update(new LiveStatistics(1,1,1,1,1,1));
        panel.stats.update(new LiveStatistics(5,2,3,4,5,6));
        panel.stats.update(new LiveStatistics(3,3,3,3,3,3));
        panel.stats.update(new LiveStatistics(2,1,1,1,1,1));
        panel.stats.update(new LiveStatistics(1,1,1,1,1,1));
        panel.stats.update(new LiveStatistics(5,2,3,4,5,0));
        panel.stats.update(new LiveStatistics(3,3,3,3,3,3));
        panel.stats.update(new LiveStatistics(2,1,1,1,1,1));

        panel.stats.update(new LiveStatistics(5,2,3,4,5,6));
        panel.stats.update(new LiveStatistics(3,3,3,3,3,3));
        panel.stats.update(new LiveStatistics(2,1,1,1,1,1));
        panel.stats.update(new LiveStatistics(1,1,1,1,1,1));
        panel.stats.update(new LiveStatistics(5,2,3,4,5,0));
        panel.stats.update(new LiveStatistics(3,3,3,3,3,3));
        panel.stats.update(new LiveStatistics(2,1,1,1,1,1));
    }
}
