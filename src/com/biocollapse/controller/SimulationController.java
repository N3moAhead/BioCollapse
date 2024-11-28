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
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.model.Virus;
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
    private final Virus virus = new Virus(); //! Probably not needed

    // Services
    private final InfectionService infectionService = new InfectionService();
    private final MovementService movementService = new MovementService(map);
    private final SimulationService simulationService = new SimulationService();

    // Display
    private final SimulationPanel visualisation;

    public SimulationController(SimulationPanel visualisation) {
        this.visualisation = visualisation;
        initializeSimulation();
    }

    private void initializeSimulation() {
        final Block[][] initMap = map.getMap();
        visualisation.setMap(initMap);
        final List<MapPosition> workplaces = extractPositions(initMap, Block.Workplace);
        final List<MapPosition> houses = extractPositions(initMap, Block.House);
        final List<MapPosition> hospitalPositions = extractPositions(initMap, Block.House);
        createEntities(initMap, workplaces, houses, hospitalPositions);
    }

    private List<MapPosition> extractPositions(Block[][] map, Block targetBlock) {
        List<MapPosition> positions = new ArrayList<>();
        for (int row = 0; row < Map.MAP_HEIGHT; row++) {
            for (int col = 0; col < Map.MAP_WIDTH; col++) {
                if (map[row][col] == targetBlock) {
                    positions.add(new MapPosition(row, col));
                }
            }
        }
        return positions;
    }

    private void createEntities(Block[][] map, List<MapPosition> workplaces, List<MapPosition> houses, List<MapPosition> hospitalPositions) {
        for (int i = 0; i < 100; i++) {
            humans.add(new Human(
                    i % 5 == 0 ? true : false,
                    false,
                    houses.get(i % houses.size()).copy(),
                    getRandomPosition(workplaces).copy(),
                    houses.get(i % houses.size()).copy()
            ));
        }
        // Creating hospitals
        // for (MapPosition hospitalPosition : hospitalPositions) {
        //   hospitals.add(new Hospital(hospitalPosition));
        // }
    }

    private MapPosition getRandomPosition(List<MapPosition> positions) {
        return positions.get(GlobalRandom.getRandIntBetween(0, positions.size() - 1));
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
                    visualisation.update(humans, newLiveStatistics, fps);
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
            currentHuman.updateHumanGoal();
            movementService.move(currentHuman);
            // TODO: Update infections and hospitals
        }
        infectionService.updateHumansStatus(humans, tick);
    }
}
