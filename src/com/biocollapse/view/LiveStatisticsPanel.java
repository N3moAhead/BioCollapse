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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import src.com.biocollapse.model.Graph;
import src.com.biocollapse.model.GraphItem;
import src.com.biocollapse.model.LiveStatistics;

public class LiveStatisticsPanel extends JPanel {

    private final List<LiveStatistics> timelineStats;
    private GridBagLayout glb;
    private Integer row;
    private final Map<String, Graph> graphs = new HashMap<>();
    private JPanel legendPanel;

    /**
     * This panel is responsible for displaying the real time stats.
     */
    public LiveStatisticsPanel() {
        timelineStats = new ArrayList<>();
        setupLayout();
    }

    public void update(LiveStatistics currentStats) {
        timelineStats.add(currentStats);
        for (GraphItem graph : currentStats.toGraph()) {
            addPoint(graph);
        }
    }

    private void addPoint(GraphItem point) {
        Graph graph;
        if (!graphs.containsKey(point.getName())) {
            graph = new Graph();
            graph.setName(point.getName());
            graph.setColor(point.getColor());
            graph.setAlwaysHidden(point.isAlwaysHidden());
            graph.setVisible(point.isVisible());

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

        JPanel legendPanelHolder = new JPanel(new BorderLayout());
        JPanel innerLegendPanelHolder = new JPanel(new BorderLayout());
        legendPanel = new JPanel();
        glb = new GridBagLayout();
        row = 0;
        legendPanel.setLayout(glb);

        innerLegendPanelHolder.add(legendPanel, BorderLayout.WEST);
        legendPanelHolder.add(innerLegendPanelHolder, BorderLayout.NORTH);
        legendPanelHolder.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));
        JLabel text = new JLabel("Live Daten");
        text.setFont(new Font("Arial", Font.BOLD, 15));
        legendPanel.add(text, makeGbc(0, text));
        row++;
        pane.setLeftComponent(legendPanelHolder);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        revalidate();
        repaint();
        GraphPanel graph = new GraphPanel(graphs);
        graph.setBackground(Color.WHITE);
        pane.setRightComponent(graph);

        add(pane);
        revalidate();
        repaint();
    }

    private GridBagConstraints makeGbc(int x, JComponent comp) {
        return new GridBagConstraints(x, row, 1, 1, 0, 0, GridBagConstraints.WEST, 0, comp.getInsets(), 0, 0);
    }
}
