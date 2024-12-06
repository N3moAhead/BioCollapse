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
        centerPanel.add(logoLabel, BorderLayout.CENTER);

        JButton startSimulationButton = new JButton("Simulation starten");
        startSimulationButton.addActionListener(e -> {
            this.controller.showConfigScreen();
        });

        centerPanel.add(startSimulationButton, BorderLayout.SOUTH);

        add(new JLabel(BIO_COLLAPSE_VERSION_TEXT, SwingConstants.RIGHT), BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}
