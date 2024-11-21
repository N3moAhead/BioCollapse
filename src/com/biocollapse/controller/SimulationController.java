package src.com.biocollapse.controller;

import java.util.ArrayList;
import java.util.List;

import src.com.biocollapse.model.Hospital;
import src.com.biocollapse.model.Human;
import src.com.biocollapse.model.Map;
import src.com.biocollapse.model.Virus;
import src.com.biocollapse.service.InfectionService;
import src.com.biocollapse.service.MovementService;
import src.com.biocollapse.service.SimulationService;

public class SimulationController {
  // Models
  List<Hospital> hospitals = new ArrayList<Hospital>();
  List<Human> humans = new ArrayList<Human>();
  Map map = new Map();
  Virus virus = new Virus();

  // Services
  InfectionService infectionService = new InfectionService();
  MovementService movementService = new MovementService(map);
  SimulationService simulationService = new SimulationService();

  SimulationController() {}

  void runMainLoop() {
    Boolean isRunning = true;
    while (isRunning) {
      // Update humans
      for (Human currentHuman : humans) {
        // Update infections
        // Update hospitals
        // Move people
        movementService.move(currentHuman);
      }
      // TODO Render update
    }
  }
}
