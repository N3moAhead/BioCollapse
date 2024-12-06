// Authors: Lars, Johann
package src.com.biocollapse.view;

import java.awt.*;
import javax.swing.*;
import src.com.biocollapse.controller.WindowController;
import static src.com.biocollapse.controller.WindowController.BIO_COLLAPSE_TITLE;
import static src.com.biocollapse.controller.WindowController.BIO_COLLAPSE_VERSION_TEXT;

public class HomePanel extends JPanel{
    private WindowController controller;

    public HomePanel(WindowController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new BorderLayout());

        Image logoImage;
        JLabel logoLabel;
        try {
            String imageUrl = System.getProperty("user.dir") + "/images/logo.png";
            logoImage = new ImageIcon(imageUrl).getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(logoImage), SwingConstants.CENTER);
        } catch (Exception e) {
            logoLabel = new JLabel(BIO_COLLAPSE_TITLE, SwingConstants.CENTER); // Fallback
        }
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        centerPanel.add(logoLabel, BorderLayout.NORTH);

        JButton startSimulationButton = new JButton("Konfiguration starten");
        startSimulationButton.addActionListener(e -> {
            this.controller.showConfigScreen();
        });

        JLabel credits = new JLabel("<html><div style='text-align: center;'>BioCollapse<br><span style='font-weight: normal;'>Die Virus Simulationssoftware um Auswirkungen eines potenziellen Virusausbruches,<br>sowie die Effektivität von Maßnahmen zur Eindämmung der Verbreitung zu analysieren.</span><br><br>Entwickelt von<br><span style='font-weight: normal;'>Lars, Lukas, Johann<br>Ina & Sebastian</span></div></html>", SwingConstants.CENTER);
        centerPanel.add(credits, BorderLayout.CENTER);


        centerPanel.add(startSimulationButton, BorderLayout.SOUTH);

        add(new JLabel(BIO_COLLAPSE_VERSION_TEXT, SwingConstants.RIGHT), BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}
