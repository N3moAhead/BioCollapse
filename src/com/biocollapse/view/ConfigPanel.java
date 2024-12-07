// Authors: Lars, Lukas, Johann
package src.com.biocollapse.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import src.com.biocollapse.controller.WindowController;
import static src.com.biocollapse.controller.WindowController.BIO_COLLAPSE_LOGO_TEXT_PATH;
import src.com.biocollapse.model.Map;
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

    private JComboBox mapNameComboBox;

    private JButton saveButton;
    private JButton backButton;
    private final WindowController controller;
    private JPanel mainPanel;

    public ConfigPanel(WindowController controller) {
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

    private void adjustSlider(JSlider source) {
        int total = childrenRatioSlider.getValue() + adultRatioSlider.getValue() + elderlyRatioSlider.getValue();

        if (total > 100) {
            int excess = total - 100;

            if (source != null) {
                if (source == childrenRatioSlider) {
                    redistribute(adultRatioSlider, elderlyRatioSlider, excess);
                } else if (source == adultRatioSlider) {
                    redistribute(childrenRatioSlider, elderlyRatioSlider, excess);
                } else if (source == elderlyRatioSlider) {
                    redistribute(childrenRatioSlider, adultRatioSlider, excess);
                }
            }
        }
    }

    private void redistribute(JSlider slider1, JSlider slider2, int excess) {
        int value1 = slider1.getValue();
        int value2 = slider2.getValue();
        int total = value1 + value2;

        if (total > 0) {
            int reduce1 = Math.min(value1, (excess * value1) / total);
            int reduce2 = Math.min(value2, excess - reduce1);
            slider1.setValue(value1 - reduce1);
            slider2.setValue(value2 - reduce2);
        } else {
            // If both are 0, only reduce source slider
            slider1.setValue(0);
            slider2.setValue(0);
        }
    }

    private void initializeComponents() {
        // Initialize Panels
        virusPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        populationPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        measuresPanel = new JPanel(new GridLayout(10, 1, 10, 10));
        buttonPanel = new JPanel();

        // Virus Sliders
        infectionRadiusSlider = new JSlider(1, 10, GlobalConfig.config.getInfectionRadius());
        infectionProbabilitySlider = new JSlider(0, 100, GlobalConfig.config.getInfectionProbability());
        incubationTimeSlider = new JSlider(0, 5, GlobalConfig.config.getConfiguredIncubationTime());
        mortalityRateSlider = new JSlider(0, 100, GlobalConfig.config.getMortalityRisk());
        timeToDeathSlider = new JSlider(1, 10, GlobalConfig.config.getConfiguredInfectionTime());
        immunityChanceSlider = new JSlider(0, 100, GlobalConfig.config.getImmunityChance());

        // Population Sliders
        hospitalCapacitySlider = new JSlider(50, 500, GlobalConfig.config.getHospitalCapacity());
        homeIsolationProbabilitySlider = new JSlider(0, 100, GlobalConfig.config.getIsolationProbability());
        hospitalizationProbabilitySlider = new JSlider(0, 100, GlobalConfig.config.getHospitalProbability());
        childrenRatioSlider = new JSlider(0, 100, GlobalConfig.config.getChildrenRatio());
        adultRatioSlider = new JSlider(0, 100, GlobalConfig.config.getAdultRatio());
        elderlyRatioSlider = new JSlider(0, 100, GlobalConfig.config.getElderlyRatio());

        // Measures Checkboxes
        lockdownCheckBox = new JCheckBox("Ausgangssperre");
        lockdownCheckBox.setSelected(GlobalConfig.config.getLockdown());
        isolationCheckBox = new JCheckBox("Isolation");
        isolationCheckBox.setSelected(GlobalConfig.config.getIsolationMandate());
        maskMandateCheckBox = new JCheckBox("Maskenpflicht");
        maskMandateCheckBox.setSelected(GlobalConfig.config.getMaskMandate());
        schoolClosureCheckBox = new JCheckBox("Schulschließung");
        schoolClosureCheckBox.setSelected(GlobalConfig.config.getSchoolClosure());

        // Map Name ComboBox
        String[] mapNames = (String[]) Map.getMapNames().toArray(new String[0]);
        mapNameComboBox = new JComboBox<>(mapNames);
        mapNameComboBox.setSelectedItem(GlobalConfig.config.getMapName());

        saveButton = new JButton("Virus freisetzen");
        backButton = new JButton("Zum Startbildschirm");

        ChangeListener sliderListener = (ChangeEvent e) -> {
            JSlider sourcSlider = (JSlider) e.getSource();
            adjustSlider(sourcSlider);
        };

        childrenRatioSlider.addChangeListener(sliderListener);
        adultRatioSlider.addChangeListener(sliderListener);
        elderlyRatioSlider.addChangeListener(sliderListener);
    }

    private void setupLayout() {
        // add everything to the Panels
        mainPanel = new JPanel(new BorderLayout());

        // virus sliders with captions
        virusPanel.add(createSliderWithLabels(infectionRadiusSlider, "Infektionsradius:    ", 1, 10));
        virusPanel
                .add(createSliderWithLabels(infectionProbabilitySlider, "Ansteckungswahrscheinlichkeit:    ", 0, 100));
        virusPanel.add(createSliderWithLabels(incubationTimeSlider, "Inkubationszeit:    ", 0, 5));
        virusPanel.add(createSliderWithLabels(mortalityRateSlider, "Mortalitätsrate:    ", 0, 100));
        virusPanel.add(createSliderWithLabels(timeToDeathSlider, "Zeit bis Tod möglich:    ", 1, 20));
        virusPanel.add(createSliderWithLabels(immunityChanceSlider, "Immunitätschance nach Genesung:    ", 0, 100));

        // population sliders with captions
        populationPanel.add(createSliderWithLabels(hospitalCapacitySlider, "Krankenhauskapazität:    ", 100, 1000));
        populationPanel.add(createSliderWithLabels(homeIsolationProbabilitySlider,
                "Wahrscheinlichkeit für Heimquarantäne:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(hospitalizationProbabilitySlider,
                "Wahrscheinlichkeit für Krankenhausaufenthalt:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(childrenRatioSlider, "Bevölkerungsanteil Kinder:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(adultRatioSlider, "Bevölkerungsanteil Erwachsene:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(elderlyRatioSlider, "Bevölkerungsanteil Alte:    ", 0, 100));

        JPanel layoutPanel = new JPanel(new BorderLayout());
        JPanel innerLayoutPanel = new JPanel();
        innerLayoutPanel.add(new JLabel("Karte:"));
        innerLayoutPanel.add(mapNameComboBox);
        layoutPanel.add(innerLayoutPanel, BorderLayout.WEST);
        populationPanel.add(layoutPanel);

        measuresPanel.add(lockdownCheckBox);
        measuresPanel.add(isolationCheckBox);
        measuresPanel.add(maskMandateCheckBox);
        measuresPanel.add(schoolClosureCheckBox);

        addTab("Populationsdaten", populationPanel);
        addTab("Maßnahmen", measuresPanel);
        addTab("Virusparameter", virusPanel);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);

        mainPanel.add(headerPanel(), BorderLayout.NORTH);
        mainPanel.add(this, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * The header panel in the layout.
     */
    private JPanel headerPanel() {
        JPanel header = new JPanel(new BorderLayout());

        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 2));
        JLabel label = new JLabel("Konfiguration");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(label, BorderLayout.WEST);

        JLabel icon = new JLabel();
        try {
            Image originalImage = new ImageIcon(BIO_COLLAPSE_LOGO_TEXT_PATH).getImage();
            int maxTextSize = 16;
            int maxIconHeight = maxTextSize;
            int maxIconWidth = (originalImage.getWidth(null) * maxIconHeight) / originalImage.getHeight(null);
            icon.setIcon(
                    new ImageIcon(originalImage.getScaledInstance(maxIconWidth, maxIconHeight, Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {
        }
        header.add(icon, BorderLayout.EAST);

        return header;
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

        String mapName = (String) mapNameComboBox.getSelectedItem();

        GlobalConfig.config.setConfig(infectionRadius, infectionProbability, incubationTime, mortalityRate, timeToDeath,
                immunityChance,
                hospitalCapacity, isolationProbability, hospitalProbability, childrenRatio, adultRatio, elderlyRatio,
                lockdown, isolateMandate, maskMandate, schoolClosure, mapName);

        controller.showSimulationScreen();
    }
}
