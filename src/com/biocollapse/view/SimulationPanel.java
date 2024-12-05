// Authors: Lars, Johann
package src.com.biocollapse.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import src.com.biocollapse.controller.SimulationController;
import src.com.biocollapse.controller.WindowController;
import static src.com.biocollapse.controller.WindowController.BIO_COLLAPSE_LOGO_PAINTING_PATH;
import static src.com.biocollapse.controller.WindowController.BIO_COLLAPSE_LOGO_TEXT_PATH;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.LiveStatistics;

public class SimulationPanel extends JPanel {

    private final WindowController controller;
    private MapPanel map;
    private LiveStatisticsPanel stats;
    private JLabel dayText;

    /**
     * This panel is the screen for the simulation content, taking care of the layout and simulation updates.
     */
    public SimulationPanel(WindowController controller) {
        this.controller = controller;
        setupLayout();

        SimulationController simController = new SimulationController(this);
        simController.runMainLoop();

        revalidate();
        repaint();
    }

    /**
     * Set the layout of the simulation screen.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 0));

        dayText = new JLabel("[ LIVE ]", SwingConstants.CENTER);

        try {
            dayText.setIcon(new ImageIcon(new ImageIcon(BIO_COLLAPSE_LOGO_PAINTING_PATH).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {
        }

        dayText.setFont(new Font("Arial", Font.BOLD, 16));
        topBar.add(dayText, BorderLayout.EAST);

        JLabel icon = new JLabel();
        try {
            Image originalImage = new ImageIcon(BIO_COLLAPSE_LOGO_TEXT_PATH).getImage();
            int maxTextSize =  16;
            int maxIconHeight = maxTextSize;
            int maxIconWidth = (originalImage.getWidth(null) * maxIconHeight) / originalImage.getHeight(null);
            icon.setIcon(new ImageIcon(originalImage.getScaledInstance(maxIconWidth, maxIconHeight, Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {
        }
        topBar.add(icon, BorderLayout.WEST);

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
        map.update(humanData);
        stats.update(liveStatistics);
        dayText.setText("Tag " + liveStatistics.getDay()+" ");

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
    public void simulationComplete(String summary) {
        controller.showStatisticsScreen(stats.getTimelineStats(), stats.getGraphs(), summary);
    }
}
