// Authors: Lukas, Johann
package src.com.biocollapse.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * HelperUtils
 */
public class HelperUtils {

    /**
     * Export panels such as the statistics graph as JPEG.
     *
     * @throws IOException
     */
    public static void savePanelAsJPEG(JPanel panel, String filePath) throws IOException {
        BufferedImage image = new BufferedImage(
                panel.getWidth(),
                panel.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g2d = image.createGraphics();
        panel.paint(g2d);
        g2d.dispose();

        ImageIO.write(image, "jpeg", new File(filePath));
    }
}
