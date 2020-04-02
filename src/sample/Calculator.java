package sample;

import javafx.concurrent.Task;

public class Calculator extends Task<Object> {
    Draw d;
    double y;

    public Calculator(Draw draw, double y) {
        d = draw;
        this.y = y;
    }

    @Override
    protected Object call() throws Exception {
        mandelbrotSet();
        return null;
    }

    public void mandelbrotSet() {
        Complex z = new Complex(0, 0);
        Complex c = new Complex(0, 0);
        int iterations, graphX, graphY;
        for (double x = d.xLimL; x <= d.xLimU; x += d.diff) {
            iterations = 0;
            c.update(x, y);
            z.nullify();
            while (z.r < 2 && iterations < d.maxIterations) {
                z.square().add(c);
                iterations++;
            }
            graphX = d.originX + (int) (x * d.magSize);
            graphY = d.originY + (int) (y * d.magSize);
            d.screen[graphX][graphY] = iterations;
        }
    }
}
