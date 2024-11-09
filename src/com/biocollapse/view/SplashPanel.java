package src.com.biocollapse.view;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class SplashPanel extends JPanel {

    public SplashPanel() {
        // Display logo, version, build number...
        add(new JTextField("This is the "+getClass().getName()));
    }
}
