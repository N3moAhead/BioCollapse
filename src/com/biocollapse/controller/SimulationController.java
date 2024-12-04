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
    public static double SIMULATION_MULTIPLIER = 1.0;

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
    private String simulationCompleteReason;

    /**
     * The controller takes care of the main simulation loop.
     * The simulation is displayed in the @param visualisation panel.
     */
    public SimulationController(SimulationPanel visualisation) {
        this.visualisation = visualisation;
        SIMULATION_MULTIPLIER = 1.0;
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
                    TimeUnit.MILLISECONDS.sleep((long)(SIMULATION_FRAME_DELAY/SIMULATION_MULTIPLIER));
                } catch (Exception ignored) {
                }

                double fps = lastFps; // Needed for swingutilities to access scope.
                SwingUtilities.invokeLater(() -> visualisation.update(humans, currentStats, fps));

                long tickEndTime = System.nanoTime() - tickStartTime; // Time taken for this frame in nanoseconds
                double tickTimeSeconds = tickEndTime / 1_000_000_000.0; // Convert to seconds
                lastFps = 1.0 / tickTimeSeconds;
                tick++;

                isRunning.set(!isSimulationComplete(currentStats, tick, day));
            }

            visualisation.simulationComplete(simulationCompleteReason);
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

    /**
     * Returns true and a generates a summary if the simulation is complete.
     */
    private boolean isSimulationComplete(LiveStatistics currenStatistics, int tick, int day) {
        boolean complete = false;
        StringBuilder b = new StringBuilder();
        b.append("<HTML><span style='font-weight: normal; font-size: 9px;'>");
        if (currenStatistics.getAlive() == 0) {
            b.append("Die Simulation wurde am Tag ").append(day).append(" beendet, da alle Menschen an dem Virus gestorben sind.");
            complete = true;
        } else if (currenStatistics.getInfected() == 0) {
            b.append("Die Simulation wurde am Tag ").append(day).append(" beendet, da es keine infizierten Personen mehr gab. Dank dir wurde die Menschheit gerettet!");
            complete = true;
        } else if (tick > (Config.SIMULATION_ONE_DAY_TICKS * Config.SIMULATION_MAX_DAYS)) {
            complete = true;
            b.append("Die Simulation wurde am Tag ").append(day).append(" beendet, da noch einige Menschen leben. Die Population scheint trotz Virus stabil zu sein. Gut gemacht!");
        }
        if (tick > (Config.SIMULATION_ONE_DAY_TICKS * Config.SIMULATION_MAX_DAYS) || currenStatistics.getInfected() == 0) {
            if (currenStatistics.getAlive() == 1) {
                b.append("<br>Nur ein einsamer Mensch hat überlebt.");
            } else {
                b.append("<br>Es haben ")
                .append(currenStatistics.getAlive())
                .append(" Menschen ")
                .append("(")
                .append(humans.size()*currenStatistics.getAlive()/100)
                .append("%)")
                .append(" überlebt.");
            }
            b
            .append("<br>Insgesamt sind ")
            .append(currenStatistics.getImmune())
            .append(" Menschen ")
            .append("(")
            .append(humans.size()*currenStatistics.getImmune()/100)
            .append("%)")
            .append(" immun geworden und ")
            .append(currenStatistics.getDeaths())
            .append(" Menschen ")
            .append("(")
            .append(humans.size()*currenStatistics.getDeaths()/100)
            .append("%)")
            .append(" gestorben");
        }
        b.append("</span></HTML>");
        simulationCompleteReason = b.toString();
        return complete;
    }
}
