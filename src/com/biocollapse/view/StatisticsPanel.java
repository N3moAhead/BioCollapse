// Authors: Lars, Johann
package src.com.biocollapse.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import src.com.biocollapse.controller.WindowController;
import src.com.biocollapse.model.LiveStatistics;
import java.io.FileWriter;
import java.io.IOException;

public class StatisticsPanel extends JPanel {
    private WindowController controller;
    private List<LiveStatistics> timelineStats;

    public StatisticsPanel(WindowController controller, List<LiveStatistics> timelineStats) {
        this.controller = controller;
        this.timelineStats = timelineStats;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel largeMessage = new JLabel("Simulation abgeschlossen!", SwingConstants.CENTER);
        largeMessage.setFont(new Font("Arial", Font.BOLD, 24));
        largeMessage.setForeground(Color.BLACK);
        add(largeMessage, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        JLabel smallMessage = new JLabel("Exportieren Sie die Daten über den Button.", SwingConstants.CENTER);
        smallMessage.setFont(new Font("Arial", Font.PLAIN, 16));
        smallMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(smallMessage);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton exportButton = new JButton("Exportieren");
        exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exportButton.addActionListener(e -> exportJSON());
        centerPanel.add(exportButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void exportJSON() {
        try {
            String fileName = System.getProperty("user.dir") + "/simulation_results.json";
            FileWriter writer = new FileWriter(fileName);

            writer.write("[\n");
            for (int i = 0; i < timelineStats.size(); i++) {
                LiveStatistics stat = timelineStats.get(i);
                // TODO Convert all stats to JSON
                writer.write(stat.toJSON());
                if (i < timelineStats.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]");
            writer.close();

            JOptionPane.showMessageDialog(this, "Daten erfolgreich exportiert nach: " + fileName, 
                    "Export erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Exportieren der Daten.", 
                    "Export fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        controller.showHomeScreen();
    }
}
