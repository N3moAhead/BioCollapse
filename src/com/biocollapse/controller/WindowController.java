package src.com.biocollapse.controller;

import src.com.biocollapse.view.ConfigPanel;
import src.com.biocollapse.view.HomePanel;
import src.com.biocollapse.view.SplashPanel;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class WindowController extends JFrame{
    private SplashPanel splash;
    private HomePanel homeScreen;
    private ConfigPanel configPanel;
    
    public WindowController(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,10,screenSize.width, screenSize.height);
        setVisible(true);

        setTitle("BioCollapse");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showSplashScreen(){
        splash = new SplashPanel(this);
        setContentPane(splash.getPanel());
        setVisible(true);

        splash.startLoadingAnimation();   
    }

    public void showHomeScreen(){
        homeScreen = new HomePanel(this);
        setContentPane(homeScreen.getPanel());
        revalidate();
    }

    public void showConfigScreen(){
        configPanel = new ConfigPanel(this);
        setContentPane(configPanel.getMainPanel());
        revalidate();
        repaint();
    }
}
