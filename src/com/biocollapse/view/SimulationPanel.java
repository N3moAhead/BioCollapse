// Authors: Lars, Johann
package src.com.biocollapse.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import src.com.biocollapse.controller.SimulationController;
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

        SimulationController simController = new SimulationController(this);
        simController.runMainLoop();

        revalidate();
        repaint();
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
    public void update(List<Human> humanData, LiveStatistics liveStatistics, double fps) {
        // this.fps.setText(String.format("%.1f", fps) + " FPS (" + SIMULATION_FRAME_DELAY + " ms delay active)");
        map.update(humanData);
        stats.update(liveStatistics);

        revalidate();
        repaint();
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
}