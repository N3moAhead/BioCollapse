package src.com.biocollapse.view;

import javax.swing.*;
import java.awt.*;
import java.util.jar.JarEntry;

import src.com.biocollapse.controller.WindowController;

public class ConfigScreen {
    private JTabbedPane tabbedPane;
    private JPanel virusPanel;
    private JPanel populationPanel;
    private JPanel measuresPanel;
    private JPanel buttonPanel;

    private JSlider infectionRadiusSlider;
    private JSlider infectionProbabilitySlider;
    private JSlider incubationTimeSlider;
    private JSlider mortalityRateSlider;
    private JSlider timeToDeathSlider;
    private JSlider immunityChanceSlider;

    private JSlider hospitalCapacitySlider;
    private JSlider homeIsolationProbabilitySlider;
    private JSlider hospitalizationProbabilitySlider;
    private JSlider childrenRatioSlider;
    private JSlider adultRatioSlider;
    private JSlider elderlyRatioSlider;

    private JCheckBox lockdownCheckBox;
    private JCheckBox isolationCheckBox;
    private JCheckBox maskMandateCheckBox;
    private JCheckBox schoolClosureCheckBox;

    private JButton saveButton;
    private JButton backButton;
    private WindowController controller;
    private JPanel mainPanel;

    public ConfigScreen(WindowController controller){
        this.controller = controller;

        initializeComponents();
        setupLayout();
        addEventListener();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void initializeComponents() {
        // Initialisiere die Panels und Sliders
        tabbedPane = new JTabbedPane();
        virusPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        populationPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        measuresPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        buttonPanel = new JPanel();

        // Virusparameter Sliders
        infectionRadiusSlider = new JSlider();
        infectionProbabilitySlider = new JSlider();
        incubationTimeSlider = new JSlider();
        mortalityRateSlider = new JSlider();
        timeToDeathSlider = new JSlider();
        immunityChanceSlider = new JSlider();

        // Population und Krankenhausslider
        hospitalCapacitySlider = new JSlider();
        homeIsolationProbabilitySlider = new JSlider();
        hospitalizationProbabilitySlider = new JSlider();
        childrenRatioSlider = new JSlider();
        adultRatioSlider = new JSlider();
        elderlyRatioSlider = new JSlider();

        // Maßnahmen Checkboxes
        lockdownCheckBox = new JCheckBox("Ausgangssperre");
        isolationCheckBox = new JCheckBox("Isolation");
        maskMandateCheckBox = new JCheckBox("Maskenpflicht");
        schoolClosureCheckBox = new JCheckBox("Schulschließung");

        // Buttons für Speichern und Zurück
        saveButton = new JButton("Speichern");
        backButton = new JButton("Zurück");
    }

    private void setSimpleLayout() {
        mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(new JLabel("Infektionsradius"));
        mainPanel.add(infectionRadiusSlider);
        mainPanel.add(new JLabel("Ansteckungswahrscheinlichkeit"));
        mainPanel.add(infectionProbabilitySlider);
        mainPanel.add(new JLabel("Inkubationszeit"));
        mainPanel.add(incubationTimeSlider);
        mainPanel.add(new JLabel("Mortalitätsrate"));
        mainPanel.add(mortalityRateSlider);
        mainPanel.add(new JLabel("Zeit bis Tod möglich"));
        mainPanel.add(timeToDeathSlider);
        mainPanel.add(new JLabel("Immunitätschance nach Genesung"));
        mainPanel.add(immunityChanceSlider);

        // Füge Slider und Checkboxen zu Maßnahmen-Panel hinzu
        mainPanel.add(new JLabel("Krankenhauskapazität"));
        mainPanel.add(hospitalCapacitySlider);
        mainPanel.add(new JLabel("Wahrscheinlichkeit für Heimquarantäne"));
        mainPanel.add(homeIsolationProbabilitySlider);
        mainPanel.add(new JLabel("Wahrscheinlichkeit für Krankenhausaufenthalt"));
        mainPanel.add(hospitalizationProbabilitySlider);
        mainPanel.add(new JLabel("Bevölkerungsanteil Kinder"));
        mainPanel.add(elderlyRatioSlider);
        mainPanel.add(childrenRatioSlider);
        mainPanel.add(new JLabel("Bevölkerungsanteil Erwachsene"));
        mainPanel.add(adultRatioSlider);
        mainPanel.add(new JLabel("Bevölkerungsanteil Alte"));

        mainPanel.add(lockdownCheckBox);
        mainPanel.add(isolationCheckBox);
        mainPanel.add(maskMandateCheckBox);
        mainPanel.add(schoolClosureCheckBox);
    }

    private void setupLayout() {
        mainPanel = new JPanel(new BorderLayout());

        // Füge Slider zu Virusparameter-Panel hinzu
        virusPanel.add(new JLabel("Infektionsradius"));
        virusPanel.add(infectionRadiusSlider);
        virusPanel.add(new JLabel("Ansteckungswahrscheinlichkeit"));
        virusPanel.add(infectionProbabilitySlider);
        virusPanel.add(new JLabel("Inkubationszeit"));
        virusPanel.add(incubationTimeSlider);
        virusPanel.add(new JLabel("Mortalitätsrate"));
        virusPanel.add(mortalityRateSlider);
        virusPanel.add(new JLabel("Zeit bis Tod möglich"));
        virusPanel.add(timeToDeathSlider);
        virusPanel.add(new JLabel("Immunitätschance nach Genesung"));
        virusPanel.add(immunityChanceSlider);

        // Füge Slider und Checkboxen zu Maßnahmen-Panel hinzu
        populationPanel.add(new JLabel("Krankenhauskapazität"));
        populationPanel.add(hospitalCapacitySlider);
        populationPanel.add(new JLabel("Wahrscheinlichkeit für Heimquarantäne"));
        populationPanel.add(homeIsolationProbabilitySlider);
        populationPanel.add(new JLabel("Wahrscheinlichkeit für Krankenhausaufenthalt"));
        populationPanel.add(hospitalizationProbabilitySlider);
        populationPanel.add(new JLabel("Bevölkerungsanteil Kinder"));
        populationPanel.add(childrenRatioSlider);
        populationPanel.add(new JLabel("Bevölkerungsanteil Erwachsene"));
        populationPanel.add(adultRatioSlider);
        populationPanel.add(new JLabel("Bevölkerungsanteil Alte"));
        populationPanel.add(elderlyRatioSlider);

        measuresPanel.add(lockdownCheckBox);
        measuresPanel.add(isolationCheckBox);
        measuresPanel.add(maskMandateCheckBox);
        measuresPanel.add(schoolClosureCheckBox);

        // Tabs zur TabbedPane hinzufügen
        tabbedPane.addTab("Virusparameter", virusPanel);
        tabbedPane.addTab("Populationsdaten", populationPanel);
        tabbedPane.addTab("Maßnahmen", measuresPanel);

        // Buttons zentriert anordnen
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);

        // Füge Komponenten zum Hauptpanel hinzu
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addEventListener() {
        saveButton.addActionListener(e -> notifyController());
        backButton.addActionListener(e -> controller.showHomeScreen());
    }

    private void notifyController() {
        // Hier könnten die aktuellen Slider- und Checkbox-Werte erfasst und an den Controller weitergeleitet werden
        controller.showHomeScreen();
    }
}
