package src.com.biocollapse.controller;

import src.com.biocollapse.view.SplashScreen;
import javax.swing.JFrame;

public class WindowController {
    private JFrame frame;
    
    public WindowController(){
        frame = new JFrame("Biocollapse");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    public void startSplashScreen(){
        SplashScreen splash = new SplashScreen(this);
        frame.setContentPane(splash.getPanel());
        frame.setVisible(true);

        splash.startLoadingAnimation();
        
    }

    public void notifyClose(){
        frame.dispose();
    }
}
