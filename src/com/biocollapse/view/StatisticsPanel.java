package src.com.biocollapse.view;

import java.util.List;
import javax.swing.JPanel;
import src.com.biocollapse.controller.WindowController;
import src.com.biocollapse.model.LiveStatistics;

public class StatisticsPanel extends JPanel{
    private WindowController controller;

    public StatisticsPanel(WindowController controller, List<LiveStatistics> timelineStats) {
        this.controller = controller;
        // TODO: Implement layout.
    }
}
