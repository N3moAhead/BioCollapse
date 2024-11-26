package src.com.biocollapse.controller;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import src.com.biocollapse.model.LiveStatistics;
import src.com.biocollapse.view.ConfigPanel;
import src.com.biocollapse.view.HomePanel;
import src.com.biocollapse.view.SimulationPanel;
import src.com.biocollapse.view.SplashPanel;
import src.com.biocollapse.view.StatisticsPanel;

public class WindowController extends JFrame{
    public static final String BIO_COLLAPSE_LOGO_PATH = System.getProperty("user.dir") + "/images/logo.png";
    public static final String BIO_COLLAPSE_TITLE = "BioCollapse";
    public static final String BIO_COLLAPSE_VERSION_NUM = "1.0";
    public static final String BIO_COLLAPSE_VERSION_TEXT = "Version "+BIO_COLLAPSE_VERSION_NUM;

    private SplashPanel splashScreen;
    private HomePanel homeScreen;
    private ConfigPanel configScreen;
    private SimulationPanel simulationScreen;
    private StatisticsPanel statisticsScreen;
    
    public WindowController(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,10,screenSize.width, screenSize.height);
        setVisible(true);

        setTitle(BIO_COLLAPSE_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            setIconImage(new ImageIcon(BIO_COLLAPSE_LOGO_PATH).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        } catch (Exception ignored) {
        }
    }

    public void showSplashScreen(){
        splashScreen = new SplashPanel(this);
        setContentPane(splashScreen.getPanel());
        setVisible(true);

        splashScreen.startLoadingAnimation();   
    }

    public void showHomeScreen(){
        homeScreen = new HomePanel(this);
        setContentPane(homeScreen.getPanel());
        revalidate();
    }

    public void showConfigScreen(){
        configScreen = new ConfigPanel(this);
        setContentPane(configScreen.getMainPanel());
        revalidate();
        repaint();
    }

    public void showSimulationScreen(){
        simulationScreen = new SimulationPanel(this);
        setContentPane(simulationScreen);
        revalidate();
        
        // Following can be used to test the statistics Screen.
        
        // List<LiveStatistics> timelineStats = new ArrayList<LiveStatistics>();

        // for (int i = 0; i < 10; i++) {
        //     LiveStatistics test = new LiveStatistics(100, 15, 70, 10, 2, 43.5);
        //     timelineStats.add(test);
        // }
        // showStatisticsScreen(timelineStats);
    }

    public void showStatisticsScreen(List<LiveStatistics> timelineStats){
        statisticsScreen = new StatisticsPanel(this, timelineStats);
        setContentPane(statisticsScreen);
        revalidate();
    }
}
