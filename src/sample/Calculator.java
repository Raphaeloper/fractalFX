package sample;

import javafx.concurrent.Task;

public class Calculator extends Task<int[][]> {
    Draw d;

    public Calculator(Draw draw) {
        d = draw;
    }

    @Override
    protected int[][] call() throws Exception {
        Complex z = new Complex(0, 0);
        Complex c = new Complex(0, 0);
        int[][] screen = new int[Draw.WIDTH + 1][Draw.HEIGHT + 1];
        int iterations, graphX, graphY;
        for (double x = d.xLimL; x <= d.xLimU; x += d.diff) {
            for (double y = d.yLimL; y <= d.yLimU; y += d.diff) {
                iterations = 0;
                c.update(x, y);
                z.nullify();
                while (z.r < 2 && iterations < Draw.maxIterations) {
                    z.square().add(c);
                    iterations++;
                }
                graphX = d.ORIGIN_X + (int) (x * d.magSize);
                graphY = d.ORIGIN_Y + (int) (y * d.magSize);
                screen[graphX][graphY] = iterations;
            }
        }
        return screen;
    }
}
