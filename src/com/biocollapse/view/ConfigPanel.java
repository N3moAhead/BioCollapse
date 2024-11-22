package src.com.biocollapse.view;

import java.awt.*;
import javax.swing.*;
import src.com.biocollapse.controller.WindowController;
import src.com.biocollapse.util.GlobalConfig;

public class ConfigPanel extends JTabbedPane {
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

    public ConfigPanel(WindowController controller){
        this.controller = controller;

        initializeComponents();
        setupLayout();
        addEventListener();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel createSliderWithLabels(JSlider slider, String title, int min, int max) {
        JPanel sliderPanel = new JPanel(new BorderLayout());
        
        JLabel minLabel = new JLabel(String.valueOf(min));
        JLabel maxLabel = new JLabel(String.valueOf(max));
        JLabel currentValueLabel = new JLabel(String.valueOf(slider.getValue()));

        JPanel titleAndValue = new JPanel(new BorderLayout());
        titleAndValue.add(new JLabel(title), BorderLayout.WEST);
        titleAndValue.add(currentValueLabel, BorderLayout.CENTER);

        
        JPanel titleAndSlider = new JPanel(new BorderLayout());
        titleAndSlider.add(titleAndValue, BorderLayout.NORTH);
        titleAndSlider.add(slider, BorderLayout.CENTER);
        
        slider.addChangeListener(e -> currentValueLabel.setText(String.valueOf(slider.getValue())));
    
        sliderPanel.add(minLabel, BorderLayout.WEST);
        sliderPanel.add(titleAndSlider, BorderLayout.CENTER);
        sliderPanel.add(maxLabel, BorderLayout.EAST);
        
        return sliderPanel;
    }
    

    private void initializeComponents() {
        // Initialize Panels
        virusPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        populationPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        measuresPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        buttonPanel = new JPanel();

        // Virus Sliders
        infectionRadiusSlider = new JSlider(1, 10, 6);
        infectionProbabilitySlider = new JSlider(0, 100, 50);
        incubationTimeSlider = new JSlider(0, 5, 3);
        mortalityRateSlider = new JSlider(0, 100, 3);
        timeToDeathSlider = new JSlider(1, 20, 14);
        immunityChanceSlider = new JSlider(0, 100, 90);

        // Population Sliders
        hospitalCapacitySlider = new JSlider(100, 1000, 300);
        homeIsolationProbabilitySlider = new JSlider(0, 100, 50);
        hospitalizationProbabilitySlider = new JSlider(0, 100, 80);
        childrenRatioSlider = new JSlider(0, 100, 25);
        adultRatioSlider = new JSlider(0, 100, 50);
        elderlyRatioSlider = new JSlider(0, 100, 25);

        // Measures Checkboxes
        lockdownCheckBox = new JCheckBox("Ausgangssperre");
        isolationCheckBox = new JCheckBox("Isolation");
        maskMandateCheckBox = new JCheckBox("Maskenpflicht");
        schoolClosureCheckBox = new JCheckBox("Schulschließung");

        saveButton = new JButton("Speichern");
        backButton = new JButton("Zurück");
    }

    private void setupLayout() {
        // add everything to the Panels
        mainPanel = new JPanel(new BorderLayout());

        // virus sliders with captions
        virusPanel.add(createSliderWithLabels(infectionRadiusSlider, "Infektionsradius:    ", 1, 10));
        virusPanel.add(createSliderWithLabels(infectionProbabilitySlider, "Ansteckungswahrscheinlichkeit:    ", 0, 100));
        virusPanel.add(createSliderWithLabels(incubationTimeSlider, "Inkubationszeit:    ", 0, 5));
        virusPanel.add(createSliderWithLabels(mortalityRateSlider, "Mortalitätsrate:    ", 0, 100));
        virusPanel.add(createSliderWithLabels(timeToDeathSlider, "Zeit bis Tod möglich:    ", 1, 20));
        virusPanel.add(createSliderWithLabels(immunityChanceSlider, "Immunitätschance nach Genesung:    ", 0, 100));

        // population sliders with captions
        populationPanel.add(createSliderWithLabels(hospitalCapacitySlider, "Krankenhauskapazität:    ", 100, 1000));
        populationPanel.add(createSliderWithLabels(homeIsolationProbabilitySlider, "Wahrscheinlichkeit für Heimquarantäne:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(hospitalizationProbabilitySlider, "Wahrscheinlichkeit für Krankenhausaufenthalt:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(childrenRatioSlider, "Bevölkerungsanteil Kinder:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(adultRatioSlider, "Bevölkerungsanteil Erwachsene:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(elderlyRatioSlider, "Bevölkerungsanteil Alte:    ", 0, 100));


        measuresPanel.add(lockdownCheckBox);
        measuresPanel.add(isolationCheckBox);
        measuresPanel.add(maskMandateCheckBox);
        measuresPanel.add(schoolClosureCheckBox);

        addTab("Virusparameter", virusPanel);
        addTab("Populationsdaten", populationPanel);
        addTab("Maßnahmen", measuresPanel);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);

        mainPanel.add(this, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addEventListener() {
        saveButton.addActionListener(e -> notifyController());
        backButton.addActionListener(e -> controller.showHomeScreen());
    }

    private void notifyController() {
        // Save Config parameters
        int infectionRadius = infectionRadiusSlider.getValue();
        int infectionProbability = infectionProbabilitySlider.getValue();
        int incubationTime = incubationTimeSlider.getValue();
        int mortalityRate = mortalityRateSlider.getValue();
        int timeToDeath = timeToDeathSlider.getValue();
        int immunityChance = immunityChanceSlider.getValue();

        int hospitalCapacity = hospitalCapacitySlider.getValue();
        int isolationProbability = homeIsolationProbabilitySlider.getValue();
        int hospitalProbability = hospitalizationProbabilitySlider.getValue();
        int childrenRatio = childrenRatioSlider.getValue();
        int adultRatio = adultRatioSlider.getValue();
        int elderlyRatio = elderlyRatioSlider.getValue();

        boolean lockdown = lockdownCheckBox.isSelected();
        boolean isolateMandate = isolationCheckBox.isSelected();
        boolean maskMandate = maskMandateCheckBox.isSelected();
        boolean schoolClosure = schoolClosureCheckBox.isSelected();

        GlobalConfig.config.setConfig(infectionRadius, infectionProbability, incubationTime, mortalityRate, timeToDeath, immunityChance, 
        hospitalCapacity, isolationProbability, hospitalProbability, childrenRatio, adultRatio, elderlyRatio, 
        lockdown, isolateMandate, maskMandate, schoolClosure);

        System.out.println(GlobalConfig.config.toString());

        controller.showSimulationScreen();
    }
}