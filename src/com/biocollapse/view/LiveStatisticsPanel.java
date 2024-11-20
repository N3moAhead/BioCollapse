package src.com.biocollapse.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import src.com.biocollapse.model.LiveStatistics;

public class LiveStatisticsPanel extends JPanel {

    private final List<LiveStatistics> timelineStats;

    /**
     * This panel is responsible for displaying the real time stats.
     */
    public LiveStatisticsPanel() {
        timelineStats = new ArrayList<>();
        // TODO: Implement layout.
    }

    public void update(LiveStatistics currentStats) {
        timelineStats.add(currentStats);

        // TODO: Display current statistics.
    }

    /**
     * Get a list with the history of statistics.
     */
    public List<LiveStatistics> getTimelineStats() {
        return (timelineStats == null) ? new ArrayList<>() : timelineStats;
    }
}