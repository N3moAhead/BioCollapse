package src.com.biocollapse.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import src.com.biocollapse.controller.WindowController;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.LiveStatistics;

public class SimulationPanel extends JPanel{
    private WindowController controller;
    private MapPanel map;
    private LiveStatisticsPanel stats;

    public SimulationPanel(WindowController controller) {
        this.controller = controller;
        setupLayout();
    }

    private void setupLayout() {
    	JPanel innerMapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        map = new MapPanel();
        innerMapPanel.add(map);
        setLayout(new BorderLayout());
        add(innerMapPanel, BorderLayout.WEST);
        
        
        add(map.legendPanel(), BorderLayout.NORTH);

        stats = new LiveStatisticsPanel();
        add(stats);
    }

    /**
     * Updates the map.
     */
    public void updateMap(Block[][] mapData, List<Human> humanData, LiveStatistics liveStatistics) {
        map.update(mapData, humanData);
        stats.update(liveStatistics);
    }

    /**
     * Call when the simulation ended to display the statistics.
     */
    public void simulationComplete() {
        controller.showStatisticsScreen(stats.getTimelineStats());
    }
    
    
    /**
     * Map debugging. TODO: Remove later.
     * @param args
     */
    public static void main(String[] args) {
    	MapPanel.DEBUG_MAP = true;
    	JFrame frame = new JFrame();
    	
    	Dimension d = MapPanel.getMapDimension();
    	Dimension newDim = new Dimension((int) d.getWidth()+10, (int)d.getHeight()+75);
    	frame.setSize(newDim);
    	frame.setMinimumSize(newDim);
    	frame.setPreferredSize(newDim);
    	
    	frame.setVisible(true);
    	frame.add(new SimulationPanel(null));
      }
}
