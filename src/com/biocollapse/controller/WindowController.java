package src.com.biocollapse.controller;

import src.com.biocollapse.view.ConfigScreen;
import src.com.biocollapse.view.HomeScreen;
import src.com.biocollapse.view.SplashScreen;
import javax.swing.JFrame;

public class WindowController {
    private JFrame frame;
    private SplashScreen splash;
    private HomeScreen homeScreen;
    private ConfigScreen configScreen;
    
    public WindowController(){
        frame = new JFrame("Biocollapse");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    public void showSplashScreen(){
        splash = new SplashScreen(this);
        frame.setContentPane(splash.getPanel());
        frame.setVisible(true);

        splash.startLoadingAnimation();   
    }

    public void showHomeScreen(){
        homeScreen = new HomeScreen(this);
        frame.setContentPane(homeScreen.getPanel());
        frame.revalidate();
    }

    public void showConfigScreen(){
        configScreen = new ConfigScreen(this);
        frame.setContentPane(configScreen.getMainPanel());
        frame.revalidate();
        frame.repaint();
    }
}
