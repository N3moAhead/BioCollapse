// Authors: Lukas, Johann
package src.com.biocollapse.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.EnumMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Human;

public class MapPanel extends JPanel {

    private static float CELL_SIZE;
    private static int width = 100;
    private static int height = 100;
    private Block[][] map;
    private List<Human> humanData;
    private java.util.Map<Block, Color> legend;

    private static final Color COLOR_INFECTED = new Color(220, 38, 38);
    private static final Color COLOR_HEALTHY = new Color(248, 250, 252);

    private static final String STATUS_INFECTED = "Infected";
    private static final String STATUS_HEALTHY = "Healthy";

    /**
     * This panel is responsible for displaying the map.
     */
    public MapPanel() {
        initLegend();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                calculateCellSize();
            }
        });
    }

    /**
     * Calculates the map size based on the panels height.
     */
    private void calculateCellSize() {
        SwingUtilities.invokeLater(() -> {
            CELL_SIZE = (float) (getSize().getHeight() / height);
            setDimensions();
            revalidate();
            repaint();
        });
    }

    /**
     * Called every frame to display the updated human positions.
     */
    public void update(List<Human> humanData) {
        if (map == null) {
            throw new IllegalStateException("The map has not been initialized. Please call setMap(...) first.");
        }
        this.humanData = humanData;
        repaint();
    }

    /**
     * Sets the layout of the map.
     */
    public void setMap(Block[][] map) {
        this.map = map;
        width = map.length;
        height = map[0].length;
        calculateCellSize();
    }

    /**
     * Set the panel size.
     */
    private void setDimensions() {
        Dimension d = getMapDimension();
        setSize(d);
        setMinimumSize(d);
        setPreferredSize(d);
    }

    public JPanel legendPanel() {
        if (legend == null) {
            return new JPanel();
        }
        JPanel layoutPanel = new JPanel(new BorderLayout());
        JPanel legendPanel = new JPanel();
        for (Block b : Block.values()) {
            legendPanel.add(createLegendItem(b.name(), legend.get(b), true));
        }

        legendPanel.add(createLegendItem(STATUS_HEALTHY, COLOR_HEALTHY, false));
        legendPanel.add(createLegendItem(STATUS_INFECTED, COLOR_INFECTED, false));

        layoutPanel.add(legendPanel, BorderLayout.WEST);
        return layoutPanel;
    }

    private void initLegend() {
        legend = new EnumMap<>(Block.class);
        legend.put(Block.Grass, new Color(Block.Grass.getArgb()));
        legend.put(Block.Path, new Color(Block.Path.getArgb()));
        legend.put(Block.Hospital, new Color(Block.Hospital.getArgb()));
        legend.put(Block.House, new Color(Block.House.getArgb()));
        legend.put(Block.Workplace, new Color(Block.Workplace.getArgb()));
    }

    private JPanel createLegendItem(String name, Color c, boolean drawBackground) {
        JPanel item = new JPanel();
        item.add(new JLabel(name));
        if (drawBackground) {
            item.setBackground(c);
            item.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        } else {
            item.setBorder(BorderFactory.createLineBorder(c, 3));
            item.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 50));
        }
        return item;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (map == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        drawMap(g2d);
        drawHumans(g2d);
    }

    private void drawMap(Graphics2D g2d) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                drawObject(map[x][y], x, y, g2d);
            }
        }
    }

    private void drawHumans(Graphics2D g2d) {
        if (humanData != null) {
            for (Human human : humanData) {
                if (human.isAlive()) {
                    drawHuman(human, g2d);
                }
            }
        }
    }

    private void drawObject(Block block, int x, int y, Graphics2D g2d) {
        float cellX = x * CELL_SIZE;
        float cellY = y * CELL_SIZE;

        g2d.setColor(legend.get(block));

        g2d.fill(new Rectangle2D.Float(cellX, cellY, CELL_SIZE, CELL_SIZE));

        g2d.setColor(Color.BLACK);
        g2d.draw(new Rectangle2D.Float(cellX, cellY, CELL_SIZE, CELL_SIZE));
    }

    private void drawHuman(Human human, Graphics2D g2d) {
        int x = human.getPos().getRow();
        int y = human.getPos().getCol();

        float cellX = x * CELL_SIZE;
        float cellY = y * CELL_SIZE;

        Color c = human.isInfected() ? COLOR_INFECTED : COLOR_HEALTHY;
        g2d.setColor(c);
        Ellipse2D.Float oval = new Ellipse2D.Float();
        oval.setFrame(cellX + 1, cellY + 1, CELL_SIZE - 2, CELL_SIZE - 2);
        g2d.fill(oval);
    }

    public static Dimension getMapDimension() {
        return new Dimension((int) (width * CELL_SIZE), (int) (height * CELL_SIZE));
    }
}