// Authors: Lars, Johann
package src.com.biocollapse.view;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import src.com.biocollapse.controller.WindowController;
import static src.com.biocollapse.controller.WindowController.BIO_COLLAPSE_LOGO_PATH;

public class SplashPanel extends JPanel {
    private JProgressBar progressBar;
    private JLabel logoLabel;
    private WindowController controller;

    public SplashPanel(WindowController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // load logo image into JPanel
        try {
            String imageUrl = BIO_COLLAPSE_LOGO_PATH;
            Image logoImage = new ImageIcon(imageUrl).getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(logoImage), SwingConstants.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            logoLabel = new JLabel("BioCollapse", SwingConstants.CENTER); // Fallback
        }
        add(logoLabel, BorderLayout.CENTER);

        // Fake-Ladebalken erstellen
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(1920,100));
        progressBar.setUI(new RoundedProgressBarUI());
        add(progressBar, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return this;
    }

    public void startLoadingAnimation() {
        // Fill progressBar with timer animation
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int progress = 0;

            @Override
            public void run() {
                progress += 5;
                progressBar.setValue(progress);
                if (progress >= 100) {
                    timer.cancel();
                    controller.showHomeScreen();
                }
            }
        }, 0, 100);
    }

    // User defined ProgressBarUI with rounded Edges
    private static class RoundedProgressBarUI extends BasicProgressBarUI {
        @Override
        protected void paintDeterminate(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Hintergrundfarbe und abgerundetes Rechteck für den Fortschrittbalken
            int width = progressBar.getWidth();
            int height = progressBar.getHeight();
            int arcSize = 100; // Rundungsgrad der Ecken
            
            // Backround (inaktiv)
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRoundRect(0, 0, width, height, arcSize, arcSize);

            // Progressbar (aktiv)
            int fillWidth = (int) (width * progressBar.getPercentComplete());
            g2.setColor(Color.decode("#C4D7EA"));
            g2.fillRoundRect(0, 0, fillWidth, height, arcSize, arcSize);

            // Fshow progress text in center
            g2.setColor(Color.BLACK);
            String progressText = progressBar.getString();
            FontMetrics metrics = g2.getFontMetrics(progressBar.getFont());
            int textX = (width - metrics.stringWidth(progressText)) / 2;
            int textY = (height + metrics.getHeight()) / 2 - metrics.getDescent();
            g2.drawString(progressText, textX, textY);

            g2.dispose();
        }
    }
}

