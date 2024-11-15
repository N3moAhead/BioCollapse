package src.com.biocollapse.view;

import javax.swing.*;

import src.com.biocollapse.controller.WindowController;

import java.awt.*;

public class HomePanel extends JPanel{
    private JButton startSimulationButton;
    private JLabel versionLabel;
    private WindowController controller;
    private Image logoImage;
    private JLabel logoLabel;

    public HomePanel(WindowController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new BorderLayout());

        try {
            String imageUrl = System.getProperty("user.dir") + "/images/logo.png";
            logoImage = new ImageIcon(imageUrl).getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(logoImage), SwingConstants.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            logoLabel = new JLabel("BioCollapse", SwingConstants.CENTER); // Fallback
        }
        centerPanel.add(logoLabel, BorderLayout.CENTER);


        startSimulationButton = new JButton("Simulation starten");
        startSimulationButton.addActionListener(e -> {
            this.controller.showConfigScreen();
        });

        centerPanel.add(startSimulationButton, BorderLayout.SOUTH);

        // TODO: make version number dynamicly
        versionLabel = new JLabel("Version 1.0", SwingConstants.RIGHT);
        add(versionLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return this;
    }
}
