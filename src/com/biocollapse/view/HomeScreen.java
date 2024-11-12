package src.com.biocollapse.view;

import javax.swing.*;

import src.com.biocollapse.controller.WindowController;

import java.awt.*;

public class HomeScreen {
    private JPanel panel;
    private JButton startSimulationButton;
    private JButton configScreenButton;
    private JLabel versionLabel;
    private Image backgroundImage;
    private WindowController controller;

    public HomeScreen(WindowController controller) {
        this.controller = controller;

        // Initialisiert das Hauptmenü-Panel
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Start-Simulation-Button erstellen
        startSimulationButton = new JButton("Simulation starten");
        startSimulationButton.addActionListener(e -> {
            // An dieser Stelle würde der Controller den Wechsel zum nächsten Screen steuern
            JOptionPane.showMessageDialog(panel, "Wechsel zum ConfigScreen");
        });
        panel.add(startSimulationButton, BorderLayout.CENTER);

        // Versionsnummer anzeigen
        versionLabel = new JLabel("Version 1.0", SwingConstants.RIGHT);
        panel.add(versionLabel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }
}
