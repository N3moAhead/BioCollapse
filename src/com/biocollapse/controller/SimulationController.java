package src.com.biocollapse.controller;

import java.util.ArrayList;
import java.util.List;

import src.com.biocollapse.model.Block;
import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.MapPosition;
import src.com.biocollapse.model.Virus;
import src.com.biocollapse.service.InfectionService;
import src.com.biocollapse.service.MovementService;
import src.com.biocollapse.service.SimulationService;
import src.com.biocollapse.util.GlobalRandom;

public class SimulationController {
  // Models
  private final List<Hospital> hospitals = new ArrayList<>();
  private final List<Human> humans = new ArrayList<>();
  private final Map map = new Map();
  private final Virus virus = new Virus(); //! Probably not needed

  // Services
  private final InfectionService infectionService = new InfectionService();
  private final MovementService movementService = new MovementService(map);
  private final SimulationService simulationService = new SimulationService();

  public SimulationController() {
    initializeSimulation();
  }

  private void initializeSimulation() {
    final Block[][] initMap = map.getMap();
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
        false,
        false,
        houses.get(i % houses.size()),
        getRandomPosition(workplaces),
        getRandomPosition(houses)
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
    while (isRunning) {
      updateHumans();
      // TODO: Render update
    }
  }

  private void updateHumans() {
    for (Human currentHuman : humans) {
      movementService.move(currentHuman);
      // TODO: Update infections and hospitals
    }
  }
}
