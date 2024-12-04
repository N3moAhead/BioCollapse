// Authors: Lars, Johann
package src.com.biocollapse.view;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import src.com.biocollapse.controller.WindowController;
import static src.com.biocollapse.controller.WindowController.BIO_COLLAPSE_LOGO_TEXT_PATH;
import src.com.biocollapse.model.Graph;
import src.com.biocollapse.model.LiveStatistics;
import src.com.biocollapse.util.GlobalConfig;
import src.com.biocollapse.util.HelperUtils;

public class StatisticsPanel extends JPanel {
    private final WindowController controller;
    private final List<LiveStatistics> timelineStats;
    private static final String EXPORT_FILE_NAME = System.getProperty("user.dir") + "/simulation_results.json";
    private static final String EXPORT_IMAGE_NAME = System.getProperty("user.dir") + "/simulation_results_graph.jpg";
    private GraphPanel graph;

    /**
     * This screen displays the finals results and allows the user to export them aswell.
     * It is shown in the end of the simulation.
     */
    public StatisticsPanel(WindowController controller, List<LiveStatistics> timelineStats, Map<String, Graph> graphs) {
        this.controller = controller;
        this.timelineStats = timelineStats;
        
        setLayout(new BorderLayout());

        JPanel layoutPanel = new JPanel(new BorderLayout());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 2));
        JLabel icon = new JLabel();
        try {
            Image originalImage = new ImageIcon(BIO_COLLAPSE_LOGO_TEXT_PATH).getImage();
            int maxTextSize =  16;
            int maxIconHeight = maxTextSize;
            int maxIconWidth = (originalImage.getWidth(null) * maxIconHeight) / originalImage.getHeight(null);
            icon.setIcon(new ImageIcon(originalImage.getScaledInstance(maxIconWidth, maxIconHeight, Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {
        }
        topBar.add(icon, BorderLayout.EAST);

        JLabel label = new JLabel("Ergebniss der Simulation", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        topBar.add(label, BorderLayout.WEST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setBackground(Color.WHITE);

        JButton exportButton = new JButton("Ergebniss als JSON exportieren");
        exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exportButton.addActionListener(e -> exportJSON());
        centerPanel.add(exportButton);

        centerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton button = new JButton("Graphen als Bild exportieren");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> exportImage());
        centerPanel.add(button);

        centerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        button = new JButton("Wiederholen");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> replay());
        centerPanel.add(button);

        centerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        button = new JButton("Aktuelle Parameter anpassen");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> configScreen());
        centerPanel.add(button);

        centerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        button = new JButton("Zum Startbildschirm");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> homeScreen());
        centerPanel.add(button);

        centerPanel.setBorder(new TitledBorder("Optionen"));
        layoutPanel.add(centerPanel, BorderLayout.NORTH);
        setUpGraph(layoutPanel, graphs);
        add(layoutPanel, BorderLayout.CENTER);
        add(topBar, BorderLayout.NORTH);
    }

    /**
     * Show the final graph.
     */
    private void setUpGraph(JPanel targetpanel, Map<String, Graph> graphs) {
        JPanel layoutPanel = new JPanel(new BorderLayout());
        graph = new GraphPanel(graphs);
        graph.setVisiblePoints(graphs.get(LiveStatistics.STAT_ALIVE).size());
        graph.setDrawConnections(true);
        graph.setDrawPoints(!(graphs.get(LiveStatistics.STAT_ALIVE).size() > 20));
        graph.setBackground(Color.WHITE);
        graph.setReverseFinalState(true);
        layoutPanel.add(graph, BorderLayout.CENTER);
        layoutPanel.setBorder(new TitledBorder("Verlauf"));
        targetpanel.add(layoutPanel, BorderLayout.CENTER);
    }

    /**
     * Export the graph as image.
     */
    private void exportImage() {
        try {
            HelperUtils.savePanelAsJPEG(graph, EXPORT_IMAGE_NAME);
            JOptionPane.showMessageDialog(this, "Daten erfolgreich exportiert nach: " + EXPORT_IMAGE_NAME, "Export erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Exportieren der Daten.", "Export fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Export the statistics in JSON format.
     */
    private void exportJSON() {
        try {
            try (FileWriter writer = new FileWriter(EXPORT_FILE_NAME)) {
                writer.write("[\n");
                for (int i = 0; i < timelineStats.size(); i++) {
                    LiveStatistics stat = timelineStats.get(i);
                    writer.write(stat.toJSON());
                    if (i < timelineStats.size() - 1) {
                        writer.write(",\n");
                    }
                }
                writer.write("\n]");
            }

            JOptionPane.showMessageDialog(this, "Daten erfolgreich exportiert nach: " + EXPORT_FILE_NAME, "Export erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Exportieren der Daten.", "Export fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Navigate to home screen.
     */
    private void homeScreen() {
        GlobalConfig.resetToDefault();
        controller.showHomeScreen();
    }

    /**
     * Navigate to config screen.
     */
    private void configScreen() {
        controller.showConfigScreen();
    }

    /**
     * Show replay.
     */
    private void replay() {
        controller.showSimulationScreen();
    }
}
