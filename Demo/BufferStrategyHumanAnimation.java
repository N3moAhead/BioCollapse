package Demo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.Random;

class Human {
    int x, y; // Position on the grid
    Color color; // Color representing the human
    int speedX, speedY; // Movement speed in X and Y directions

	static int width, height, pixelSize;

    public Human(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        // Random speed for each human (can be negative for movement in the opposite direction)
        Random rand = new Random();
        this.speedX = rand.nextInt(7) - 3; // Speed between -3 and +3 pixels per frame
        this.speedY = rand.nextInt(7) - 3;
    }

    // Update the position based on speed
    public void move() {
        x += speedX;
        y += speedY;

        // Bounce off the edges of the panel
        if (x < 0 || x > width - pixelSize) speedX = -speedX;
        if (y < 0 || y > width - pixelSize) speedY = -speedY;
    }
}

public class BufferStrategyHumanAnimation extends JFrame {
    private final int pixelSize = 1;
    private final int numHumans = 100000; // 100k humans
    final int framesToDraw = 100;
    private Human[] humans;
    private BufferStrategy bufferStrategy;
    private VolatileImage offscreenImage; // Offscreen buffer

    public BufferStrategyHumanAnimation() {
        setTitle("BufferStrategy Human Animation");
        setSize(1000, 1000);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Initialize the humans
        humans = new Human[numHumans];
        Random rand = new Random();
        for (int i = 0; i < numHumans; i++) {
            int x = rand.nextInt(getWidth() / pixelSize) * pixelSize;
            int y = rand.nextInt(getHeight() / pixelSize) * pixelSize;
            Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            humans[i] = new Human(x, y, color);
        }

        // Create buffer strategy
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();

        // Create the offscreen image
        offscreenImage = createVolatileImage(getWidth(), getHeight());
    }

    public void startAnimation() {
		Human.width = getWidth();
		Human.height = getHeight();
		Human.pixelSize = pixelSize;
        double[] times = new double[framesToDraw];
        //while (true) {
        for (int i=0;i<framesToDraw;i++) {
            // Use the offscreen image for drawing
            long start = System.nanoTime();
            do {
                if (offscreenImage.validate(getGraphicsConfiguration()) == VolatileImage.IMAGE_INCOMPATIBLE) {
                    // Recreate if image is incompatible
                    offscreenImage = createVolatileImage(getWidth(), getHeight());
                }

                Graphics offscreenGraphics = offscreenImage.getGraphics();
                // Clear offscreen image
                offscreenGraphics.setColor(Color.BLACK);
                offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());

                // Draw humans on the offscreen image
                for (Human human : humans) {
                    offscreenGraphics.setColor(human.color);
                    offscreenGraphics.fillRect(human.x, human.y, pixelSize, pixelSize);
                    human.move();
                }

                // Dispose of the offscreen graphics
                offscreenGraphics.dispose();

                // Get the graphics object from BufferStrategy
                Graphics g = bufferStrategy.getDrawGraphics();
                // Draw the offscreen image onto the screen
                g.drawImage(offscreenImage, 0, 0, null);
                g.dispose();

                // Show the buffer
                bufferStrategy.show();
                

            } while (offscreenImage.contentsLost()); // Retry if contents of VolatileImage were lost
            
            long end = System.nanoTime();
            times[i] = (end - start )/1e6;
            // Sleep for ~60 FPS
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double total = 0;
        double min = times[0];
        double max = times[0];
        for (double time : times) {
            total += time;
            if (time > max) {
                max = time;
            }
            if (time < min) {
                min = time;
            }
        }
        System.out.println("avg frametime of " + total/framesToDraw + "ms (" + 1000/(total/framesToDraw) + " fps) over: " + framesToDraw + " frames.");
        System.out.println("min:" + min + "ms (" + 1000/min + " fps)");
        System.out.println("max:" + max + "ms (" + 1000/max + " fps)");
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        BufferStrategyHumanAnimation window = new BufferStrategyHumanAnimation();
        window.startAnimation();
    }
}
