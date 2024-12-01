package src.com.biocollapse.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import src.com.biocollapse.model.Graph;

public class GraphPanel extends JPanel {

    private final Map<String, Graph> graphs;
    private int width;
    private int height;
    private double cellSize;
    private int highestVisibleNum;

    private int visiblePoints = 50;
    private boolean drawPoints = true;
    private boolean drawConnections = true;

    private static final int PADDING_TOP = 20;
    private static final int PADDING_BOTTOM = 20;
    private static int PADDING_LEFT = 10;
    private static final int PADDING_RIGHT = 20;

    public GraphPanel(Map<String, Graph> graphs) {
        this.graphs = graphs;
        Dimension dim = new Dimension(600, 600);
        setSize(600, 600);
        setPreferredSize(dim);

        width = getWidth();
        height = getHeight();
        setVisiblePoints(visiblePoints);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                height = getHeight();
                width = getWidth();
                setVisiblePoints(visiblePoints);
                revalidate();
                repaint();
            }
        });
    }

    /**
     * The amount of points to be visible at most.
     * @param visiblePoints
     */
    public void setVisiblePoints(int visiblePoints) {
        this.visiblePoints = visiblePoints;
        cellSize = (width - PADDING_LEFT - PADDING_RIGHT) / visiblePoints;
    }

    /**
     * Wheter to draw or not draw round points for each point. Setting to false will activate connections.
     * @param drawPoints
     */
    public void setDrawPoints(boolean drawPoints) {
        this.drawPoints = drawPoints;

        if (!drawPoints) {
            drawConnections = true;
        }
    }

    /**
     * Wheter to draw or not draw connections between points. Setting to false will activate points.
     * @param drawConnections
     */
    public void setDrawConnections(boolean drawConnections) {
        this.drawConnections = drawConnections;

        if (!drawConnections) {
            drawPoints = true;
        }
    }
    
    public int getVisiblePoints() {
        return visiblePoints;
    }

    public boolean isDrawPoints() {
        return drawPoints;
    }

    public boolean isDrawConnections() {
        return drawConnections;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        highestVisibleNum = getHighestVisibleNum();
        legendY = 0;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        g.drawString("amount", 4, 10);
        g.drawString("time", width - 20 - PADDING_RIGHT, height - 7);
        g.drawString("0", 2, height - 7);

        drawLeftString(String.valueOf(highestVisibleNum), g2d, 2, PADDING_TOP + 5);
        drawLeftString(String.valueOf((float) highestVisibleNum / 2), g2d, 2, height / 2 + 5);
        drawLeftString(String.valueOf((float) highestVisibleNum / 4 + highestVisibleNum / 2), g2d, 2, height / 4 + 5);
        drawLeftString(String.valueOf((float) highestVisibleNum / 4), g2d, 2, height / 4 * 3 + 5);

        g2d.drawLine(PADDING_LEFT, height - PADDING_BOTTOM, width - PADDING_RIGHT, height - PADDING_BOTTOM); // X-axis
        g2d.drawLine(PADDING_LEFT, height - PADDING_BOTTOM, PADDING_RIGHT, PADDING_BOTTOM);                 // Y-axis

        for (Entry<String, Graph> entry : graphs.entrySet()) {
            drawPoints(entry.getValue(), g2d);
        }

        drawLegend(g2d);
    }

    private void drawLegend(Graphics2D g2d) {
        int legendSize = getLongestLegendLength(g2d);
        g2d.setFont(new Font("Arial", Font.PLAIN, 8));
        for (Entry<String, Graph> entry : graphs.entrySet()) {
            drawLegendString(entry.getKey(), entry.getValue().getColor(), g2d, legendSize);
        }
    }


    private int legendY = 0;
    
    /**
     * Draw legend String.
     */
    private void drawLegendString(String text, Color c, Graphics2D g2d, int legendSize) {
        g2d.setColor(c);
        int lineStart = width - legendSize - 20;
        int lineEnd = width - legendSize;

        g2d.drawLine(lineStart, 10 + legendY, lineEnd, 10 + legendY);
        legendY += g2d.getFontMetrics().getHeight();

        g2d.setColor(Color.BLACK);
        g2d.drawString(text, lineEnd+5, legendY+4);
    }

    /**
     * Get the longest width of a legend text.
     */
    private int getLongestLegendLength(Graphics2D g2d) {
        int legendWidth = 0;
        for (Entry<String, Graph> entry : graphs.entrySet()) {
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(entry.getKey());
            if (textWidth > legendWidth) {
                legendWidth = textWidth;
            }
        }
        return legendWidth;
    }

    /**
     * Draw the Strings on the left and adjust padding accordingly.
     */
    private void drawLeftString(String text, Graphics2D g2d, int x, int y) {
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.setColor(Color.BLACK);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);

        if (textWidth > PADDING_LEFT) {
            PADDING_LEFT = textWidth + 10;
        }
        g2d.drawString(text, x, y);
    }

    /**
     * Get the highest value of all points.
     */
    private int getHighestVisibleNum() {
        int highestNum = 0;
        for (Entry<String, Graph> entry : graphs.entrySet()) {
            highestNum = (entry.getValue().getHighestValue() > highestNum) ? entry.getValue().getHighestValue() : highestNum;
        }
        return highestNum;
    }

    /**
     * Draw the points of a graph.
     */
    private void drawPoints(Graph graph, Graphics2D g2d) {
        double prevX = PADDING_LEFT;
        double prevY = height - PADDING_BOTTOM;

        g2d.setColor(graph.getColor());

        int i = 0;
        int size = 0;
        for (int j = graph.size()-1; j >= 0; j--) {
            size+=cellSize;
            if (size > getWidth()) {
                break;
            }
            double x = i * cellSize + PADDING_LEFT;
            double y = (height- PADDING_BOTTOM) - ((double)(height - 2*  PADDING_BOTTOM) / (double) highestVisibleNum * graph.get(j).getValue());

            if (drawPoints) {
                Ellipse2D.Float oval = new Ellipse2D.Float();
                oval.setFrame(x - 3, y - 3, 6, 6);
                g2d.fill(oval);
            }

            if (i != 0 && drawConnections) {
                g2d.draw(new Line2D.Double(prevX, prevY, x, y)); // Vertical lines
            }
            prevX = x;
            prevY = y;

            i++;
        }
    }
}
