package src.com.biocollapse.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import src.com.biocollapse.model.LiveStatistics;
import static src.com.biocollapse.model.LiveStatistics.STAT_ALIVE;
import static src.com.biocollapse.model.LiveStatistics.STAT_DEATHS;
import static src.com.biocollapse.model.LiveStatistics.STAT_HEALTHY;
import static src.com.biocollapse.model.LiveStatistics.STAT_HOSPITAL_CAPACITY_RATIO;
import static src.com.biocollapse.model.LiveStatistics.STAT_IMMUNE;
import static src.com.biocollapse.model.LiveStatistics.STAT_INFECTED;
import static src.com.biocollapse.model.LiveStatistics.STAT_RECOVERED;

public class LiveStatisticsPanel extends JPanel {

    private final List<LiveStatistics> timelineStats;
    private StatItem textAlive, textInfected, textRecovered, textHealthy, textImmune, textDeaths, textHospitalCapacityRation;
    private GridBagLayout glb;
    private Integer row;

    /**
     * This panel is responsible for displaying the real time stats.
     */
    public LiveStatisticsPanel() {
        timelineStats = new ArrayList<>();
        setupLayout();
    }

    public void update(LiveStatistics currentStats) {
        timelineStats.add(currentStats);
        textDeaths.setValue(String.valueOf(currentStats.getDeaths()));
        textImmune.setValue(String.valueOf(currentStats.getImmune()));
        textInfected.setValue(String.valueOf(currentStats.getInfected()));
        textRecovered.setValue(String.valueOf(currentStats.getHealthy()));
    }

    /**
     * Get a list with the historiy of statistics.
     */
    public List<LiveStatistics> getTimelineStats() {
        return (timelineStats == null) ? new ArrayList<>() : timelineStats;
    }

    private void setupLayout() {
        glb = new GridBagLayout();
        row = 0;
        setLayout(glb);

        textAlive = new StatItem(STAT_ALIVE);
        textInfected = new StatItem(STAT_INFECTED);
        textRecovered = new StatItem(STAT_RECOVERED);
        textHealthy = new StatItem(STAT_HEALTHY);
        textImmune = new StatItem(STAT_IMMUNE);
        textDeaths = new StatItem(STAT_DEATHS);
        textHospitalCapacityRation = new StatItem(STAT_HOSPITAL_CAPACITY_RATIO);

        textAlive.add(this);
        textInfected.add(this);
        textRecovered.add(this);
        textHealthy.add(this);
        textImmune.add(this);
        textDeaths.add(this);
        textHospitalCapacityRation.add(this);

        revalidate();
        repaint();
    }

    private class StatItem {

        private final JLabel textName, textValue;

        public StatItem(String name) {
            textName = new JLabel(name);
            textValue = new JLabel("0");
            textValue.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        }

        public void setValue(String value) {
            textValue.setText(value);
        }

        public void add(JPanel target) {
            target.add(textName, makeGbc(0, textName));
            target.add(textValue, makeGbc(1, textValue));
            row++;
        }

        private GridBagConstraints makeGbc(int x, JLabel label) {
            return new GridBagConstraints(x, row, 1, 1, 0, 0, GridBagConstraints.WEST, 0, label.getInsets(), 0, 0);
        }
    }
}
