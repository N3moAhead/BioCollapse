package src.com.biocollapse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
import src.com.biocollapse.util.GlobalRandom;
import src.com.biocollapse.view.SimulationPanel;

public class SimulationController {

    public static int SIMULATION_FRAME_DELAY = 25;
    private int tick = 0;

    // Models
    private final List<Hospital> hospitals = new ArrayList<>();
    private final List<Human> humans = new ArrayList<>();
    private final Map map = new Map();

    // Services
    private final InfectionService infectionService = new InfectionService();
    private final MovementService movementService = new MovementService(map);
    private final SimulationService simulationService = new SimulationService();
    private final HospitalService hospitalService = new HospitalService();

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
        boolean isRunning = true;

        new Thread(() -> {
            double lastFps = 0.0;

            while (isRunning) {
                long startTime = System.nanoTime(); // Start time for this frame

                updateHumans();

                try {
                    // TODO: Find sweet spot. And let user fast forward or slow down (x2 / x0.5)
                    TimeUnit.MILLISECONDS.sleep(SIMULATION_FRAME_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                double fps = lastFps; // Needed for swingutilities to access scope.
                SwingUtilities.invokeLater(() -> {
                    // TODO: Get actual statistics.
                    LiveStatistics newLiveStatistics = simulationService.calculateLiveStatistics(humans, hospitals);
                    // visualisation.update(humans, newLiveStatistics, fps);

                    visualisation.update(humans, new LiveStatistics(GlobalRandom.getRandIntBetween(0, 100), GlobalRandom.getRandIntBetween(0, 50), GlobalRandom.getRandIntBetween(0, 50), GlobalRandom.getRandIntBetween(40, 60), GlobalRandom.getRandIntBetween(80, 100), GlobalRandom.getRandIntBetween(0, 50)), fps);
                });

                // TODO: Check if simulation is complete.
                boolean simulationComplete = false;
                if (simulationComplete) {
                    visualisation.simulationComplete();
                }

                long frameTimeNano = System.nanoTime() - startTime; // Time taken for this frame in nanoseconds
                double frameTimeSeconds = frameTimeNano / 1_000_000_000.0; // Convert to seconds
                lastFps = 1.0 / frameTimeSeconds;
                tick++;
            }
        }).start();
    }

    private void updateHumans() {
        for (Human currentHuman : humans) {
            movementService.updateHumanGoal(currentHuman);
            movementService.move(currentHuman);
            hospitalService.updateHospitals(hospitals, currentHuman);
        }
        infectionService.updateHumansStatus(humans, tick);
    }
}
