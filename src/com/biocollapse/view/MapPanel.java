package src.com.biocollapse.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.util.GlobalRandom;

public class MapPanel extends JPanel {

    private static final int CELL_SIZE = 8;
    private static int width = 100;
    private static int height = 100;
    private Block[][] map;
    private List<Human> humanData;
    private java.util.Map<Block, Color> legend;

    private static final Color COLOR_INFECTED = Color.RED;
    private static final Color COLOR_HEALTHY = Color.MAGENTA;

    private static final String STATUS_INFECTED = "Infected";
    private static final String STATUS_HEALTHY = "Healthy";

    /**
     * This panel is responsible for displaying the map.
     */
    public MapPanel() {
        initLegend();

        if (DEBUG_MAP) {
            setMap(doFakeMap());
            update(doFakeHumans());
			System.out.println("DEBUG_MAP="+DEBUG_MAP);
        }
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
		setDimensions();
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
        JPanel legendPanel = new JPanel();
        for (Block b : Block.values()) {
            legendPanel.add(createLegendItem(b.name(), legend.get(b)));
        }

        legendPanel.add(createLegendItem(STATUS_HEALTHY, COLOR_HEALTHY));
        legendPanel.add(createLegendItem(STATUS_INFECTED, COLOR_INFECTED));

        return legendPanel;
    }

    private void initLegend() {
        legend = new HashMap();
        legend.put(Block.Grass, Color.GREEN);
        legend.put(Block.Path, Color.GRAY);
        legend.put(Block.Hospital, Color.PINK);
        legend.put(Block.House, Color.YELLOW);
    }

    private JPanel createLegendItem(String name, Color c) {
        JPanel item = new JPanel();
        item.add(new JLabel(name));
        item.setBackground(c);
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
        for (Human human : humanData) {
            drawHuman(human, g2d);
        }
    }

    private void drawObject(Block block, int x, int y, Graphics2D g2d) {
        int cellX = x * CELL_SIZE;
        int cellY = y * CELL_SIZE;

        g2d.setColor(legend.get(block));

        g2d.fillRect(cellX, cellY, CELL_SIZE, CELL_SIZE);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
    }

    private void drawHuman(Human human, Graphics2D g2d) {
        int x = human.getPos().getRow();
        int y = human.getPos().getCol();

        int cellX = x * CELL_SIZE;
        int cellY = y * CELL_SIZE;

        Color c = human.isInfected() ? COLOR_INFECTED : COLOR_HEALTHY;
        g2d.setColor(c);
        g2d.fillOval(cellX + 1, cellY + 1, CELL_SIZE - 2, CELL_SIZE - 2);
    }

    // ----- DEBUGGING CODE ONLY TODO: Remove ----- 
    public static boolean DEBUG_MAP = true;

    public static Dimension getMapDimension() {
        return new Dimension(width * CELL_SIZE, height * CELL_SIZE);
    }

    public static Block[][] doFakeMap() {
        Block[][] map = new Block[height][width];

        Random random = GlobalRandom.getInstance();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = Block.values()[random.nextInt(Block.values().length)];
            }
        }
        return map;
    }

    public static List<Human> doFakeHumans() {
        List<Human> humanData = new ArrayList<>();

        Random random = GlobalRandom.getInstance();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rand = random.nextInt(5);
                if (!(rand > 0)) {
                    int randInfected = random.nextInt(3);
                    humanData.add(new Human((randInfected > 1), false, new MapPosition(x, y), null, null));
                }
            }
        }
        return humanData;
    }
}
