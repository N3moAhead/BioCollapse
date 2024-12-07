// Authors: Johann
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
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import src.com.biocollapse.model.Graph;
import src.com.biocollapse.model.GraphItem.GraphType;

public class GraphPanel extends JPanel {

    private final Map<String, Graph> graphs;
    private int width;
    private int height;
    private double cellSize;
    private int highestVisibleNum;
    private int legendY;

    private int visiblePoints = 50;
    private boolean drawPoints = true;
    private boolean drawConnections = true;

    private static final String TEXT_AMOUNT = "Anzahl";
    private static final String TEXT_TIME = "Zeit";

    private static final int PADDING_TOP = 20;
    private static final int PADDING_BOTTOM = 20;
    private static final int PADDING_RIGHT = 20;
    private static int PADDING_LEFT = 10;

    private static final int LEGEND_FONT_SIZE = 10;
    private static final int LEGEND_MARGIN = 10;

    private boolean reverseFinalState = false;

    public GraphPanel(Map<String, Graph> graphs) {
        this.graphs = graphs;
        Dimension dim = new Dimension(600, 600);
        setSize(600, 600);
        setPreferredSize(dim);

        width = getWidth();
        height = getHeight();
        cellSize = (width - PADDING_LEFT - PADDING_RIGHT) / visiblePoints;
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
        cellSize = ((double)(width - PADDING_LEFT - PADDING_RIGHT)) / visiblePoints;
    }

    /**
     * Whether to draw or not draw round points for each point. Setting to false will activate connections.
     * @param drawPoints
     */
    public void setDrawPoints(boolean drawPoints) {
        this.drawPoints = drawPoints;

        if (!drawPoints) {
            drawConnections = true;
        }
    }

    /**
     * Whether to draw or not draw connections between points. Setting to false will activate points.
     * @param drawConnections
     */
    public void setDrawConnections(boolean drawConnections) {
        this.drawConnections = drawConnections;

        if (!drawConnections) {
            drawPoints = true;
        }
    }

    /**
     * Reverse the graph
     * @param reverseFinalState
     */
    public void setReverseFinalState(boolean reverseFinalState) {
        this.reverseFinalState = reverseFinalState;
    }

    /**
     * @return the max amount of points that should be drawn.
     */
    public int getVisiblePoints() {
        return visiblePoints;
    }

    /**
     * @return whether or not each point should have it's own circle drawn.
     */
    public boolean isDrawPoints() {
        return drawPoints;
    }

    /**
     * @return whether or not the connections from point to point should be drawn.
     */
    public boolean isDrawConnections() {
        return drawConnections;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        highestVisibleNum = getHighestVisibleNum();
        legendY = 10;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Draw the layout details.
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        g.drawString(TEXT_AMOUNT, 4, 10);
        g.drawString(TEXT_TIME, width - 20 - PADDING_RIGHT, height - 7);
        g.drawString("0", 2, height - 7);

        // Draws the value indications for each quatre.
        drawLeftString(String.valueOf(highestVisibleNum), g2d, 2, PADDING_TOP + 5);
        drawLeftString("100%", g2d, 2, PADDING_TOP + LEGEND_FONT_SIZE +LEGEND_FONT_SIZE / 2);
        drawLeftString(String.valueOf((float) highestVisibleNum / 2), g2d, 2, height / 2 + 5);
        drawLeftString(String.valueOf((float) highestVisibleNum / 4 + highestVisibleNum / 2), g2d, 2, height / 4 + 5);
        drawLeftString(String.valueOf((float) highestVisibleNum / 4), g2d, 2, height / 4 * 3 + 5);

        // Draw X and Y lines.
        g2d.drawLine(PADDING_LEFT, height - PADDING_BOTTOM, width - PADDING_RIGHT, height - PADDING_BOTTOM);
        g2d.drawLine(PADDING_LEFT, height - PADDING_BOTTOM, PADDING_LEFT, PADDING_BOTTOM);

        // Draw the points.
        for (Entry<String, Graph> entry : graphs.entrySet()) {
            drawPoints(entry.getValue(), g2d);
        }

        g2d.setFont(new Font("Arial", Font.PLAIN, LEGEND_FONT_SIZE));
        int legendSize = getLongestLegendLength(g2d);
        drawLegendBackground(g2d, legendSize);
        drawLegend(g2d, legendSize);
    }

    /**
     * Draws the legend for the graphs.
     * @param g2d
     */
    private void drawLegend(Graphics2D g2d, int legendSize) {
        for (Entry<String, Graph> entry : graphs.entrySet()) {
            if (entry.getValue().isAlwaysHidden() || !entry.getValue().isVisible()) {
                return;
            }
            drawLegendString(entry.getKey(), entry.getValue().getColor(), g2d, legendSize);
        }
    }
    
    /**
     * Draw a legends text.
     */
    private void drawLegendString(String text, Color c, Graphics2D g2d, int legendSize) {
        g2d.setColor(c);
        int lineStart = width - legendSize - LEGEND_MARGIN * 2;
        int lineEnd = width - legendSize;

        g2d.drawLine(lineStart, LEGEND_MARGIN + legendY, lineEnd, LEGEND_MARGIN + legendY);
        legendY += LEGEND_FONT_SIZE;

        g2d.setColor(Color.BLACK);
        g2d.drawString(text, lineEnd+5, legendY+4);
    }

    /**
     * Draw the legend background.
     */
    private void drawLegendBackground(Graphics2D g2d, int legendSize) {
        g2d.setColor(Color.WHITE);
        int startX = width - legendSize -30;
        int endY = graphs.size()*LEGEND_FONT_SIZE+10;
        g2d.fill(new Rectangle2D.Double(startX, 10, legendSize +LEGEND_MARGIN * 2, endY));

        g2d.setColor(Color.BLACK);
        g2d.drawRect(startX, 10, legendSize+LEGEND_MARGIN * 2, endY);
        setMinimumSize(new Dimension(10, (endY+2*LEGEND_MARGIN)*2));
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
        return legendWidth + LEGEND_MARGIN * 2;
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
     * Draw all points of a given graph.
     */
    private void drawPoints(Graph graph, Graphics2D g2d) {
        if (graph.isAlwaysHidden() || !graph.isVisible()) {
            return;
        }
        g2d.setColor(graph.getColor());
        
        double prevX = PADDING_LEFT;
        double prevY = height - PADDING_BOTTOM;

        int i = 0;
        int size = 0;
        double percentageValue = (double)(height - 2*  PADDING_BOTTOM) / 100;
        double defaultValue = (double)(height - 2*  PADDING_BOTTOM) / (double) highestVisibleNum;

        if (reverseFinalState) {
            for (int j = 0; j <= graph.size()-1; j++) {
                size+=cellSize;
                if (size > getWidth()) {
                    break;
                }
                double x = i * cellSize + PADDING_LEFT;
                double y;
                if (graph.getType() == GraphType.PERCENTAGE) {
                    y = (height- PADDING_BOTTOM) - (percentageValue * graph.get(j).getValue());
                } else {
                    y = (height- PADDING_BOTTOM) - (defaultValue * graph.get(j).getValue());
                }

                drawPoint(prevX, prevY, x, y, g2d, i);
                prevX = x;
                prevY = y;
                i++;
            }
        } else {
            for (int j = graph.size()-1; j >= 0; j--) {
                size+=cellSize;
                if (size > getWidth()) {
                    break;
                }
                double x = i * cellSize + PADDING_LEFT;
                double y;
                if (graph.getType() == GraphType.PERCENTAGE) {
                    y = (height- PADDING_BOTTOM) - (percentageValue * graph.get(j).getValue());
                } else {
                    y = (height- PADDING_BOTTOM) - (defaultValue * graph.get(j).getValue());
                }

                drawPoint(prevX, prevY, x, y, g2d, i);
                prevX = x;
                prevY = y;
                i++;
            }
        }
    }

    /**
     * Draw the actual point and connection lines.
     */
    private void drawPoint(double prevX, double prevY, double x, double y, Graphics2D g2d, int i) {
        if (drawPoints) {
            Ellipse2D.Float oval = new Ellipse2D.Float();
            oval.setFrame(x - 3, y - 3, 6, 6);
            g2d.fill(oval);
        }
        if (drawConnections && i != 0) {
            g2d.draw(new Line2D.Double(prevX, prevY, x, y));
        }
    }
    
}
