// Authors: Lukas, Johann
package src.com.biocollapse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.LiveStatistics;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.service.HospitalService;
import src.com.biocollapse.service.InfectionService;
import src.com.biocollapse.service.MovementService;
import src.com.biocollapse.service.SimulationService;
import src.com.biocollapse.view.SimulationPanel;

public class SimulationController {

    public static int SIMULATION_FRAME_DELAY = 50;
    private int tick = 0;

    // Models
    private final List<Hospital> hospitals = new ArrayList<>();
    private final List<Human> humans = new ArrayList<>();
    private final Map map = new Map("maze");

    // Services
    private final InfectionService infectionService = new InfectionService();
    private final MovementService movementService = new MovementService(map);
    private final SimulationService simulationService = new SimulationService();
    private final HospitalService hospitalService = new HospitalService(map);

    // Display
    private final SimulationPanel visualisation;

    public SimulationController(SimulationPanel visualisation) {
        this.visualisation = visualisation;
        final Block[][] initMap = map.getMap();
        visualisation.setMap(initMap);
        simulationService.initializeSimulation(initMap, humans, hospitals);
    }

    // Main Simulation Loop
    public void runMainLoop() {
        AtomicBoolean isRunning = new AtomicBoolean(true);

        new Thread(() -> {
            double lastFps = 0.0;


            while (isRunning.get()) {
                long startTime = System.nanoTime(); // Start time for this frame
; 
                updateSimulation();
                LiveStatistics currentStats = simulationService.calculateLiveStatistics(humans, hospitals);

                try {
                    // TODO: Find sweet spot. And let user fast forward or slow down (x2 / x0.5)
                    TimeUnit.MILLISECONDS.sleep(SIMULATION_FRAME_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                double fps = lastFps; // Needed for swingutilities to access scope.
                SwingUtilities.invokeLater(() -> visualisation.update(humans, currentStats, fps));

                long frameTimeNano = System.nanoTime() - startTime; // Time taken for this frame in nanoseconds
                double frameTimeSeconds = frameTimeNano / 1_000_000_000.0; // Convert to seconds
                lastFps = 1.0 / frameTimeSeconds;
                tick++;

                isRunning.set(!isSimulationComplete(currentStats));
            }

            visualisation.simulationComplete();
        }).start();
    }

    private void updateSimulation() {
        infectionService.initInfectionUpdates();
        for (Human currentHuman : humans) {
            infectionService.updateInfectedPositions(currentHuman);
            movementService.updateHumanGoal(currentHuman, tick);
            movementService.move(currentHuman);
            hospitalService.updateHospitals(hospitals, currentHuman);
        }
        infectionService.updateHumansStatus(humans, tick);
    }

    private boolean isSimulationComplete(LiveStatistics currenStatistics){
        boolean result = false;
        
        // Check for: Max Days, No one infected, all dead
        if (currenStatistics.getInfected() == 0 || currenStatistics.getAlive() == 0 || tick > (250 * 14)) {
            result = true;
        }
        return result;
    }
}
