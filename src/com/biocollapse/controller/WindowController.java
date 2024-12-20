// Authors: Lars, Lukas, Johann
package src.com.biocollapse.controller;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import src.com.biocollapse.model.Graph;
import src.com.biocollapse.model.LiveStatistics;
import src.com.biocollapse.view.ConfigPanel;
import src.com.biocollapse.view.HomePanel;
import src.com.biocollapse.view.SimulationPanel;
import src.com.biocollapse.view.SplashPanel;
import src.com.biocollapse.view.StatisticsPanel;

public class WindowController extends JFrame {

    public static final String BIO_COLLAPSE_LOGO_PATH = System.getProperty("user.dir") + "/images/logo.png";
    public static final String BIO_COLLAPSE_LOGO_TEXT_PATH = System.getProperty("user.dir") + "/images/logo_text.png";
    public static final String BIO_COLLAPSE_LOGO_PAINTING_PATH = System.getProperty("user.dir") + "/images/logo_painting.png";
    public static final String BIO_COLLAPSE_TITLE = "BioCollapse";
    public static final String BIO_COLLAPSE_VERSION_NUM = "1.0";
    public static final String BIO_COLLAPSE_VERSION_TEXT = "Version " + BIO_COLLAPSE_VERSION_NUM;

    private SplashPanel splashScreen;
    private HomePanel homeScreen;
    private ConfigPanel configScreen;
    private SimulationPanel simulationScreen;
    private StatisticsPanel statisticsScreen;

    /**
     * The window controller takes care of the window state.
     */
    public WindowController() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 10, screenSize.width, screenSize.height);

        setTitle(BIO_COLLAPSE_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            setIconImage(new ImageIcon(BIO_COLLAPSE_LOGO_PAINTING_PATH).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        } catch (Exception ignored) {
        }

        setVisible(true);
    }

    public void showSplashScreen() {
        splashScreen = new SplashPanel(this);
        setContentPane(splashScreen);
        setVisible(true);

        splashScreen.startLoadingAnimation();
    }

    public void showHomeScreen() {
        homeScreen = new HomePanel(this);
        setContentPane(homeScreen);
        revalidate();
    }

    public void showConfigScreen() {
        configScreen = new ConfigPanel(this);
        setContentPane(configScreen.getMainPanel());
        revalidate();
        repaint();
    }

    public void showSimulationScreen() {
        SwingUtilities.invokeLater(() -> {
            invalidate();
            repaint();
            simulationScreen = new SimulationPanel(this);
            setContentPane(simulationScreen);
            invalidate();
            repaint();
        });

    }

    public void showStatisticsScreen(List<LiveStatistics> timelineStats, Map<String, Graph> graphs, String summary) {
        statisticsScreen = new StatisticsPanel(this, timelineStats, graphs, summary);
        setContentPane(statisticsScreen);
        revalidate();
    }
}
