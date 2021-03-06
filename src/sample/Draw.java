package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.*;


public class Draw extends Canvas {
    static final Toolkit tk = Toolkit.getDefaultToolkit();
    static final int WIDTH = (int) tk.getScreenSize().getWidth();
    static final int HEIGHT = (int) tk.getScreenSize().getHeight();
    static final int MAX_COLORS = 16777216;
    
    int maxIterations = 1000;                                         //increasing this will give you a more detailed fractal
    double xLimU = 4;                                                 //xLimU > xLimL      default:  4
    double xLimL = -4;                                                //                   default: -4
    double totalX = xLimU - xLimL;
    double magSize = WIDTH / totalX;                                  //magnification (in percent)
    double yLimU = HEIGHT / (2 * magSize);                            //yLimU > yLimL
    double yLimL = -yLimU;
    double diff = ((totalX) / WIDTH) / 2;
    int[][] screen = new int[WIDTH + 1][HEIGHT + 1];                  //pixel grid
    int originX = (int) (WIDTH - xLimU * magSize),                    //offset
            originY = (int) (HEIGHT - yLimU * magSize);
    int colorStep = MAX_COLORS / maxIterations;

    WritableImage wImage;

    public Draw() {
        wImage = new WritableImage(WIDTH, HEIGHT);
    }

    public void updateRange(double xU, double xL) {
        this.xLimU = xU;
        this.xLimL = xL;
        totalX = xLimU - xLimL;
        magSize = WIDTH / totalX;
        yLimU = HEIGHT / (2 * magSize);
        yLimL = -yLimU;
        diff = ((totalX) / WIDTH) / 2;
        originX = (int) (WIDTH - xLimU * magSize);
        originY = (int) (HEIGHT - yLimU * magSize);
    }

    public WritableImage paint(int y) {
        PixelWriter writer = wImage.getPixelWriter();
        double[] rgba = new double[4];
        for (int x = 0; x < WIDTH; x++) {
            intToRGB(/*MAX_COLORS - */screen[x][y] * colorStep, rgba);
            writer.setColor(x, y, new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
        }
        return wImage;
    }

    public void intToRGB(double rgb, double[] rgba) {
        rgba[0] = (rgb / 256 / 256) / 256;
        rgba[1] = (rgb / 256 % 256) / 256;
        rgba[2] = (rgb % 256) / 256;
        rgba[3] = 1.0;
    }
}
