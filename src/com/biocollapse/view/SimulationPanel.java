package src.com.biocollapse.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import src.com.biocollapse.controller.WindowController;
import src.com.biocollapse.model.LiveStatistics;
import src.com.biocollapse.model.Map;

public class SimulationPanel extends JPanel{
    private WindowController controller;
    private MapPanel map;
    private LiveStatisticsPanel stats;

    public SimulationPanel(WindowController controller) {
        this.controller = controller;
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        map = new MapPanel();
        add(map, BorderLayout.WEST);

        stats = new LiveStatisticsPanel();
        add(stats, BorderLayout.EAST);
    }

    /**
     * Show the statistics once the simulation is complete.
     */
    public void updateMap(Map liveMap, LiveStatistics liveStatistics) {
        map.update(liveMap);
        stats.update(liveStatistics);
    }

    /**
     * Call when the simulation ended to display the statistics.
     */
    public void simulationComplete() {
        controller.showStatisticsScreen(stats.getTimelineStats());
    }
}
