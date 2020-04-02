package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.*;


public class Draw extends Canvas {
    final static Toolkit tk = Toolkit.getDefaultToolkit();
    static final int WIDTH = (int) tk.getScreenSize().getWidth();
    static final int HEIGHT = (int) tk.getScreenSize().getHeight();
    static int maxIterations = 150;                                          //increasing this will give you a more detailed fractal
    private final int MAX_COLORS = 16777216;
    double xLimU = 4;                                                 //xLimU > xLimL      default:  4
    double xLimL = -4;                                                //                   default: -4
    double totalX = xLimU - xLimL;
    double magSize = WIDTH / totalX;                                  //magnification (in percent)
    double yLimU = HEIGHT / (2 * magSize);                            //yLimU > yLimL
    double yLimL = -yLimU;
    int[][] screen = new int[WIDTH][HEIGHT];                         //pixel grid
    double diff = ((totalX) / WIDTH) / 2;
    int ORIGIN_X = (int) (WIDTH - xLimU * magSize),
            ORIGIN_Y = (int) (HEIGHT - yLimU * magSize);
    private int colorStep = MAX_COLORS / maxIterations;

    public Draw() {
    }

    public void updateRange(double xU, double xL) {
        this.xLimU = xU;
        this.xLimL = xL;
        totalX = xLimU - xLimL;
        magSize = WIDTH / totalX;
        yLimU = HEIGHT / (2 * magSize);
        yLimL = -yLimU;
        diff = ((totalX) / WIDTH) / 2;
        ORIGIN_X = (int) (WIDTH - xLimU * magSize);
        ORIGIN_Y = (int) (HEIGHT - yLimU * magSize);
    }

    public WritableImage paint() {
        WritableImage wImage = new WritableImage(WIDTH, HEIGHT);
        PixelWriter writer = wImage.getPixelWriter();
        double[] rgba = new double[4];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                intToRGB(/*MAX_COLORS - */screen[x][y] * colorStep, rgba);
                writer.setColor(x, y, new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
            }
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
