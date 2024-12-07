// Authors: Lukas, Johann
package src.com.biocollapse.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import static src.com.biocollapse.controller.SimulationController.SIMULATION_MULTIPLIER;
import src.com.biocollapse.model.Graph;
import src.com.biocollapse.model.GraphItem;
import src.com.biocollapse.model.LiveStatistics;

public class LiveStatisticsPanel extends JPanel {

    private final List<LiveStatistics> timelineStats;
    private GridBagLayout glb;
    private Integer row;
    private final Map<String, Graph> graphs = new HashMap<>();
    private JPanel legendPanel;
    private JButton speedButton;
    private JButton slowButton;

    /**
     * This panel is responsible for displaying the real time stats.
     */
    public LiveStatisticsPanel() {
        timelineStats = new ArrayList<>();
        setupLayout();
    }

    /**
     * Updates the graph with the new statistics.
     */
    public void update(LiveStatistics currentStats) {
        timelineStats.add(currentStats);
        for (GraphItem graph : currentStats.toGraph()) {
            addPoint(graph);
        }
    }

    /**
     * Adds a point to the graph.
     */
    private void addPoint(GraphItem point) {
        Graph graph;
        if (!graphs.containsKey(point.getName())) {
            graph = new Graph();
            graph.setName(point.getName());
            graph.setColor(point.getColor());
            graph.setAlwaysHidden(point.isAlwaysHidden());
            graph.setVisible(point.isVisible());
            graph.setType(point.getType());
            legendPanel.add(graph.getNameLabel(), makeGbc(0, graph.getNameLabel()));
            legendPanel.add(graph.getValueLabel(), makeGbc(1, graph.getValueLabel()));
            row++;
            graphs.put(point.getName(), graph);
            revalidate();
            repaint();
        } else {
            graph = graphs.get(point.getName());
        }
        graph.addGraphItem(point);
    }

    /**
     * Get a list with the history of statistics.
     */
    public List<LiveStatistics> getTimelineStats() {
        return (timelineStats == null) ? new ArrayList<>() : timelineStats;
    }

    /**
     * Configure the layout.
     */
    private void setupLayout() {
        JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        pane.setResizeWeight(0.5);
        pane.setBorder(BorderFactory.createEmptyBorder());

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                pane.setDividerLocation(pane.getHeight() / 2);
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel legendPanelHolder = new JPanel(new BorderLayout());
        JPanel innerLegendPanelHolder = new JPanel(new BorderLayout());
        legendPanel = new JPanel();
        glb = new GridBagLayout();
        row = 0;
        legendPanel.setLayout(glb);

        innerLegendPanelHolder.add(legendPanel, BorderLayout.WEST);
        legendPanelHolder.add(innerLegendPanelHolder, BorderLayout.NORTH);
        legendPanelHolder.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));

        GraphPanel graph = new GraphPanel(graphs);
        graph.setBackground(Color.WHITE);
        pane.setRightComponent(graph);

        legendPanelHolder.setBorder(new TitledBorder("Live Daten"));
        topPanel.add(legendPanelHolder, BorderLayout.NORTH);
        topPanel.add(graphConfigPanel(graph), BorderLayout.CENTER);

        pane.setLeftComponent(topPanel);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        revalidate();
        repaint();

        add(pane);
        revalidate();
        repaint();
    }

    /**
     * The configuration panel for the user to configure the graph.
     *
     * @param graph
     * @return the layout panel with the configuration options.
     */
    private JPanel graphConfigPanel(GraphPanel graph) {
        JPanel layoutPanel = new JPanel(new BorderLayout());
        JPanel innerLayoutPanel = new JPanel(new BorderLayout());

        int configRow = 0;
        GridBagLayout configGbl = new GridBagLayout();
        JPanel panel = new JPanel(configGbl);

        JLabel speed = new JLabel("Geschwindigkeit:");
        speed.setFont(new Font("Arial", Font.PLAIN, 12));
        speed.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
        panel.add(speed, makeConfigGbc(0, speed, configRow));

        JPanel speedLayout = new JPanel();

        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.BOLD, 16));

        SIMULATION_MULTIPLIER = 1.0;
        label.setText(SIMULATION_MULTIPLIER + "X");

        speedButton = new JButton("--");
        speedButton.setBorder(BorderFactory.createCompoundBorder());
        speedButton.setFont(new Font("Arial", Font.BOLD, 16));
        speedButton.addActionListener(e -> setSpeed(label, true));
        speedLayout.add(speedButton);

        slowButton = new JButton("++");
        slowButton.setBorder(BorderFactory.createCompoundBorder());
        slowButton.setFont(new Font("Arial", Font.BOLD, 16));
        slowButton.addActionListener(e -> setSpeed(label, false));
        speedLayout.add(slowButton);
        
        speedLayout.add(label);
        panel.add(speedLayout, makeConfigGbc(1, speedLayout, configRow));
        configRow++;

        JLabel sliderLabel = new JLabel("Sichtbare Punkte (" + graph.getVisiblePoints() + "):");
        sliderLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        sliderLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 2));
        JSlider slider = new JSlider(50, 500);
        slider.setValue(graph.getVisiblePoints());
        slider.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        slider.addChangeListener((ChangeEvent arg0) -> {
            graph.setVisiblePoints(slider.getValue());
            sliderLabel.setText("Sichtbare Punkte (" + graph.getVisiblePoints() + ")");
        });
        panel.add(sliderLabel, makeConfigGbc(0, sliderLabel, configRow));
        panel.add(slider, makeConfigGbc(1, slider, configRow));
        configRow++;

        JCheckBox points = new JCheckBox("Punkte zeichnen");
        JCheckBox connections = new JCheckBox("Verbindungen zeichnen");
        connections.setSelected(graph.isDrawConnections());
        points.setSelected(graph.isDrawPoints());
        points.addChangeListener((ChangeEvent arg0) -> {
            graph.setDrawPoints(points.isSelected());
            if (!points.isSelected()) {
                connections.setSelected(graph.isDrawConnections());
            }
        });
        panel.add(points, makeConfigGbc(0, points, configRow));
        configRow++;

        connections.addChangeListener((ChangeEvent arg0) -> {
            graph.setDrawConnections(connections.isSelected());
            if (!connections.isSelected()) {
                points.setSelected(graph.isDrawPoints());
            }
        });
        panel.add(connections, makeConfigGbc(0, connections, configRow));
        configRow++;

        innerLayoutPanel.add(panel, BorderLayout.NORTH);
        layoutPanel.add(innerLayoutPanel, BorderLayout.WEST);
        layoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));
        layoutPanel.setBorder(new TitledBorder("Visualisation"));

        return layoutPanel;
    }

    /**
     * Sets the speed multiplier to defined values.
     */
    private void setSpeed(JLabel value, boolean isUp) {
        if (isUp && speedButton.isEnabled()) {
            SIMULATION_MULTIPLIER -= 0.25;
            if (SIMULATION_MULTIPLIER <= 0.25) {
                speedButton.setEnabled(false);
            }
            slowButton.setEnabled(true);
        } else if (!isUp && slowButton.isEnabled()) {
            SIMULATION_MULTIPLIER += 0.25;
            if (SIMULATION_MULTIPLIER >= 2) {
                slowButton.setEnabled(false);
            }
            speedButton.setEnabled(true);
        }
        value.setText(SIMULATION_MULTIPLIER + "X");
    }

    /**
     * Create layout constraints.
     */
    private GridBagConstraints makeGbc(int x, JComponent comp) {
        return new GridBagConstraints(x, row, 1, 1, 0, 0, GridBagConstraints.WEST, 0, comp.getInsets(), 0, 0);
    }

    /**
     * Create layout constraints.
     */
    private GridBagConstraints makeConfigGbc(int x, JComponent comp, int configRow) {
        return new GridBagConstraints(x, configRow, 1, 1, 0, 0, GridBagConstraints.WEST, 0, comp.getInsets(), 0, 0);
    }

    public Map<String, Graph> getGraphs() {
        return graphs;
    }
}
