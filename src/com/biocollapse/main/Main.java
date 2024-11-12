package src.com.biocollapse.main;

import src.com.biocollapse.controller.WindowController;
import src.com.biocollapse.model.Map;

public class Main {
  public static void main(String[] args) {
    System.out.println("Welcome to BioCollapse");
    Map map = new Map();
    map.printMap();

    // Testing SplashScreen
    
    WindowController controller = new WindowController();
    controller.startSplashScreen();
  }
}
