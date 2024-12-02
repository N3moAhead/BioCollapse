// Authors: Lars, Lukas
package src.com.biocollapse.main;

import src.com.biocollapse.controller.WindowController;

public class Main {
  public static void main(String[] args) {
    System.out.println("Welcome to BioCollapse");
    WindowController controller = new WindowController();
    controller.showSplashScreen();
  }
}
