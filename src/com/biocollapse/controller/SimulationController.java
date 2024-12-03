// Authors: Lukas, Johann
package src.com.biocollapse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.service.HospitalService;
import src.com.biocollapse.service.InfectionService;
import src.com.biocollapse.service.MovementService;
import src.com.biocollapse.service.SimulationService;
import src.com.biocollapse.view.SimulationPanel;

public class SimulationController {

    public static int SIMULATION_FRAME_DELAY = 25;
    private final static int SIMULATION_ONE_DAY_TICKS = 250;
    private final static int SIMULATION_MAX_DAYS = 250;

    // Models
    private final List<Hospital> hospitals = new ArrayList<>();
    private final List<Human> humans = new ArrayList<>();
    private final Map map = new Map();

    // Services
    private final InfectionService infectionService = new InfectionService();
    private final MovementService movementService = new MovementService(map);
    private final SimulationService simulationService = new SimulationService();
    private final HospitalService hospitalService = new HospitalService(map);

    // Display
    private final SimulationPanel visualisation;

    /**
     * The controller takes care of the main simulation loop.
     * The simulation is displayed in the @param visualisation panel.
     */
    public SimulationController(SimulationPanel visualisation) {
        this.visualisation = visualisation;
        final Block[][] initMap = map.getMap();
        visualisation.setMap(initMap);
        simulationService.initializeSimulation(initMap, humans, hospitals);
    }

    /**
     * Start the main loop of the simulation.
     */
    public void runMainLoop() {
        boolean isRunning = true;

        new Thread(() -> {
            double lastFps = 0.0;
            int tick = 0;

            while (isRunning) {
                long tickStartTime = System.nanoTime();
                int tickToDay = tick / SIMULATION_ONE_DAY_TICKS;
                int day = tickToDay < 1 ? 1 : (tickToDay + 1);

                updateSimulation(tick);
                try {
                    // TODO: Find sweet spot. And let user fast forward or slow down (x2 / x0.5)
                    TimeUnit.MILLISECONDS.sleep(SIMULATION_FRAME_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                double fps = lastFps; // Needed for swingutilities to access scope.
                SwingUtilities.invokeLater(() -> visualisation.update(humans, simulationService.calculateLiveStatistics(humans, hospitals, day), fps));

                if (day >= SIMULATION_MAX_DAYS) { // Todo: Find a good max value.
                    visualisation.simulationComplete();
                }

                long tickEndTime = System.nanoTime() - tickStartTime; // Time taken for this frame in nanoseconds
                double tickTimeSeconds = tickEndTime / 1_000_000_000.0; // Convert to seconds
                lastFps = 1.0 / tickTimeSeconds;
                tick++;
            }
        }).start();
    }

    /**
     * Updates the simulation state for the next tick.
     */
    private void updateSimulation(int tick) {
        infectionService.initInfectionUpdates();
        for (Human currentHuman : humans) {
            infectionService.updateInfectedPositions(currentHuman);
            movementService.updateHumanGoal(currentHuman, tick);
            movementService.move(currentHuman);
            hospitalService.updateHospitals(hospitals, currentHuman);
        }
        infectionService.updateHumansStatus(humans, tick);
    }
}
