package src.com.biocollapse.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import src.com.biocollapse.controller.SimulationController;
import static src.com.biocollapse.controller.SimulationController.SIMULATION_FRAME_DELAY;
import src.com.biocollapse.controller.WindowController;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.LiveStatistics;

public class SimulationPanel extends JPanel {

    private final WindowController controller;
    private MapPanel map;
    private LiveStatisticsPanel stats;
    private JLabel fps;

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
        setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

        JPanel leftMapPanel = new JPanel(new BorderLayout());

        map = new MapPanel();
        map.initLegend();

        leftMapPanel.add(map, BorderLayout.CENTER);
        JPanel legendPanel = map.legendPanel();
        legendPanel.setVisible(true);
        leftMapPanel.add(legendPanel, BorderLayout.NORTH);
  
        add(leftMapPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        add(rightPanel, BorderLayout.CENTER);

        fps = new JLabel();
        fps.setForeground(Color.RED);
        rightPanel.add(fps, BorderLayout.NORTH);

        stats = new LiveStatisticsPanel();
        rightPanel.add(stats, BorderLayout.CENTER);

        invalidate();
        repaint();
    }

    /**
     * Call each frame to update the human position and statistics.
     */
    public void update(List<Human> humanData, LiveStatistics liveStatistics, double fps) {
        this.fps.setText(String.format("%.1f", fps) + " FPS (" + SIMULATION_FRAME_DELAY + " ms delay active)");
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

    /**
     * Map debugging. TODO: Remove later.
     *
     * @param args
     */
    public static void main(String[] args) {
        MapPanel.DEBUG_MAP = true;
        JFrame frame = new JFrame();
        Dimension d = MapPanel.getMapDimension();
        Dimension newDim = new Dimension((int) d.getWidth() + 260, (int) d.getHeight() + 75);
        frame.setSize(newDim);
        frame.setMinimumSize(newDim);
        frame.setPreferredSize(newDim);
        frame.setVisible(true);
        SimulationPanel panel = new SimulationPanel(null);
        frame.add(panel);
        panel.stats.update(new LiveStatistics(10, 5, 6, 7, 9, 9));
    }
}
