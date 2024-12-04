// Authors: Lukas, Johann
package src.com.biocollapse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;
import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Config;
import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.LiveStatistics;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.service.HospitalService;
import src.com.biocollapse.service.InfectionService;
import src.com.biocollapse.service.SimulationService;
import src.com.biocollapse.view.SimulationPanel;

public class SimulationController {

    public static int SIMULATION_FRAME_DELAY = 25;

    // Models
    private final List<Hospital> hospitals = new ArrayList<>();
    private final List<Human> humans = new ArrayList<>();
    private final Map map = new Map("maze");

    // Services
    private final InfectionService infectionService = new InfectionService();
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
        AtomicBoolean isRunning = new AtomicBoolean(true);

        new Thread(() -> {
            double lastFps = 0.0;
            int tick = 0;

            while (isRunning.get()) {
                long tickStartTime = System.nanoTime();
                int tickToDay = tick / Config.SIMULATION_ONE_DAY_TICKS;
                int day = tickToDay < 1 ? 1 : (tickToDay + 1);
                LiveStatistics currentStats = simulationService.calculateLiveStatistics(humans, hospitals, day);

                updateSimulation(tick);

                updateSimulation(tick);
                try {
                    // TODO: Find sweet spot. And let user fast forward or slow down (x2 / x0.5)
                    TimeUnit.MILLISECONDS.sleep(SIMULATION_FRAME_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                double fps = lastFps; // Needed for swingutilities to access scope.
                SwingUtilities.invokeLater(() -> visualisation.update(humans, currentStats, fps));

                long tickEndTime = System.nanoTime() - tickStartTime; // Time taken for this frame in nanoseconds
                double tickTimeSeconds = tickEndTime / 1_000_000_000.0; // Convert to seconds
                lastFps = 1.0 / tickTimeSeconds;
                tick++;

                isRunning.set(!isSimulationComplete(currentStats, tick));
            }

            visualisation.simulationComplete();
        }).start();
    }

    /**
     * Updates the simulation state for the next tick.
     */
    private void updateSimulation(int tick) {
        infectionService.initInfectionUpdates();
        for (Human currentHuman : humans) {
            if (currentHuman.isAlive()) {
                infectionService.updateInfectedPositions(currentHuman);
                currentHuman.updateHumanGoal(map, tick);
                currentHuman.move(map);
                hospitalService.updateHospitals(hospitals, currentHuman);
            }
        }
        infectionService.updateHumansStatus(humans, tick);
    }

    private boolean isSimulationComplete(LiveStatistics currenStatistics, int tick) {
        boolean result = false;

        // Check for: Max Days, No one infected, all dead
        if (currenStatistics.getInfected() == 0 || currenStatistics.getAlive() == 0
                || tick > (Config.SIMULATION_ONE_DAY_TICKS * Config.SIMULATION_MAX_DAYS)) {
            result = true;
        }
        return result;
    }
}
