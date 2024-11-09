package src.com.biocollapse.view;

import javax.swing.JFrame;

public class UserInterface extends JFrame{

    /**
     * Creates an user interface inside a window.
     */
    public UserInterface() {
        configureWindow();
        displaySplashScreen();
    }

    private void configureWindow() {
        setSize(600, 400);
        setVisible(true);
    }

    private void displaySplashScreen() {
        add(new SplashPanel());
        revalidate();
        repaint();
    }
}
