// Authors: Lars, Lukas, Johann, Sebastian
package src.com.biocollapse.view;

import java.awt.*;
import java.text.NumberFormat;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import src.com.biocollapse.controller.WindowController;
import static src.com.biocollapse.controller.WindowController.BIO_COLLAPSE_LOGO_TEXT_PATH;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.util.GlobalConfig;

public class ConfigPanel extends JPanel {

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

    private JComboBox<String> mapNameComboBox;
    private JFormattedTextField seedFormattedTextField;

    private JButton saveButton;
    private JButton backButton;
    private final WindowController controller;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    public ConfigPanel(WindowController controller) {
        this.controller = controller;

        initializeComponents();
        setupLayout();
        addEventListener();
    }

    /**
     * Revalidates the values of the current config from the GlobalConfig.
     */
    public void revalidateConfig() {
        infectionRadiusSlider.setValue(GlobalConfig.config.getInfectionRadius());
        infectionProbabilitySlider.setValue(GlobalConfig.config.getInfectionProbability());
        incubationTimeSlider.setValue(GlobalConfig.config.getConfiguredIncubationTime());
        mortalityRateSlider.setValue(GlobalConfig.config.getMortalityRisk());
        timeToDeathSlider.setValue(GlobalConfig.config.getConfiguredInfectionTime());
        immunityChanceSlider.setValue(GlobalConfig.config.getImmunityChance());
        
        // Population sliders
        hospitalCapacitySlider.setValue(GlobalConfig.config.getHospitalCapacity());
        homeIsolationProbabilitySlider.setValue(GlobalConfig.config.getIsolationProbability());
        hospitalizationProbabilitySlider.setValue(GlobalConfig.config.getHospitalProbability());
        childrenRatioSlider.setValue(GlobalConfig.config.getChildrenRatio());
        adultRatioSlider.setValue(GlobalConfig.config.getAdultRatio());
        elderlyRatioSlider.setValue(GlobalConfig.config.getElderlyRatio());
        
        // Measures Checkboxes
        lockdownCheckBox.setSelected(GlobalConfig.config.getLockdown());
        isolationCheckBox.setSelected(GlobalConfig.config.getIsolationMandate());
        maskMandateCheckBox.setSelected(GlobalConfig.config.getMaskMandate());
        schoolClosureCheckBox.setSelected(GlobalConfig.config.getSchoolClosure());

        // Map Name ComboBox
        mapNameComboBox.setSelectedItem(GlobalConfig.config.getMapName());

        // Seed Textfield
        seedFormattedTextField.setValue(GlobalConfig.config.getSeed());

        revalidate();
        repaint();
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
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();

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
        schoolClosureCheckBox = new JCheckBox("Geschäftsschließungen");
        schoolClosureCheckBox.setSelected(GlobalConfig.config.getSchoolClosure());

        // Map Name ComboBox
        String[] mapNames = (String[]) Map.getMapNames().toArray(new String[0]);
        mapNameComboBox = new JComboBox<>(mapNames);
        mapNameComboBox.setSelectedItem(GlobalConfig.config.getMapName());

        // Seed Textfield
        NumberFormatter numberFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        numberFormatter.setValueClass(Long.class); // Allow long values
        numberFormatter.setAllowsInvalid(false); // Prevent invalid characters
        numberFormatter.setMinimum(Long.MIN_VALUE);
        numberFormatter.setMaximum(Long.MAX_VALUE);
        seedFormattedTextField = new JFormattedTextField(numberFormatter);
        seedFormattedTextField.setValue(GlobalConfig.config.getSeed());
        seedFormattedTextField.setColumns(15);

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
        populationPanel.add(createSliderWithLabels(hospitalCapacitySlider, "Krankenhauskapazität:    ", 50, 500));
        populationPanel.add(createSliderWithLabels(homeIsolationProbabilitySlider,
                "Wahrscheinlichkeit für Heimquarantäne:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(hospitalizationProbabilitySlider,
                "Wahrscheinlichkeit für Krankenhausaufenthalt:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(childrenRatioSlider, "Bevölkerungsanteil Kinder:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(adultRatioSlider, "Bevölkerungsanteil Erwachsene:    ", 0, 100));
        populationPanel.add(createSliderWithLabels(elderlyRatioSlider, "Bevölkerungsanteil Alte:    ", 0, 100));

        JPanel layoutPanelMap = new JPanel(new BorderLayout());
        JPanel innerLayoutPanelMap = new JPanel();
        innerLayoutPanelMap.add(new JLabel("Karte:"));
        innerLayoutPanelMap.add(mapNameComboBox);
        layoutPanelMap.add(innerLayoutPanelMap, BorderLayout.WEST);
        populationPanel.add(layoutPanelMap);

        JPanel layoutPanelSeed = new JPanel(new BorderLayout());
        JPanel innerLayoutPanelSeed = new JPanel();
        innerLayoutPanelSeed.add(new JLabel("Seed:"));
        innerLayoutPanelSeed.add(seedFormattedTextField);
        layoutPanelSeed.add(innerLayoutPanelSeed, BorderLayout.WEST);
        populationPanel.add(layoutPanelSeed);

        measuresPanel.add(lockdownCheckBox);
        measuresPanel.add(isolationCheckBox);
        measuresPanel.add(maskMandateCheckBox);
        measuresPanel.add(schoolClosureCheckBox);

        tabbedPane.addTab("Populationsdaten", populationPanel);
        tabbedPane.addTab("Maßnahmen", measuresPanel);
        tabbedPane.addTab("Virusparameter", virusPanel);

        add(tabbedPane, BorderLayout.CENTER);
        add(new ConfigHistoryPanel(this), BorderLayout.EAST);

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
        long seed = (long) seedFormattedTextField.getValue();

        GlobalConfig.config.setConfig(infectionRadius, infectionProbability, incubationTime, mortalityRate, timeToDeath,
                immunityChance,
                hospitalCapacity, isolationProbability, hospitalProbability, childrenRatio, adultRatio, elderlyRatio,
                lockdown, isolateMandate, maskMandate, schoolClosure, mapName, seed);

        saveConfig();
        controller.showSimulationScreen();
    }

    /**
     * Store the config object as a file.
     */
    private void saveConfig() {
         LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formattedTime = now.format(formatter);

        try (FileOutputStream fileOut = new FileOutputStream(ConfigHistoryPanel.FOLDER_HISTORY+"/config "+formattedTime); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(GlobalConfig.getConfig());
        } catch (Exception ignored) {
        }
    }
}
