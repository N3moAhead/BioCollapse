package src.com.biocollapse.main;

import src.com.biocollapse.model.Map;
import src.com.biocollapse.view.UserInterface;

public class Main {
  public static void main(String[] args) {
    System.out.println("Welcome to BioCollapse");
    Map map = new Map();
    map.printMap();

    UserInterface userInterface = new UserInterface();
  }
}
