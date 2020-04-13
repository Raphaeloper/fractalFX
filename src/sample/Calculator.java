package sample;

import javafx.concurrent.Task;

public class Calculator extends Task<Object> {
    public static final int M_SET = 1234;
    public static final int J_SET = 5678;
    int id;
    Draw d;
    double y;
    Complex c;

    public Calculator(Draw draw, double y) {
        d = draw;
        this.y = y;
        c = new Complex(0, 0);
        id = M_SET;
    }

    public Calculator(Draw draw, Complex c) {
        d = draw;
        this.c = c;
        id = J_SET;
    }

    @Override
    protected Object call() throws Exception {
        if (id == M_SET)
            mandelbrotSet();
        else if (id == J_SET)
            juliaSet();
        return null;
    }

    public void juliaSet() {
        Complex z = new Complex(0, 0);
        double R = /*(1+Math.sqrt(1+4*c.r))/2*/ 0.788, xtemp;
        int iterations, graphX, graphY;
        for (double x = d.xLimL; x <= d.xLimU; x += d.diff) {
            for (double y = d.yLimL; y <= d.yLimU; y += d.diff) {
                iterations = 0;
                z.update(x, y);
                while (z.a * z.a + z.b * z.b < R * R && iterations < d.maxIterations) {
                    xtemp = z.a * z.a - z.b * z.b;
                    z.b = 2 * z.a * z.b + c.b;
                    z.a = xtemp + c.a;
                    iterations++;
                }
                graphX = d.originX + (int) (x * d.magSize);
                graphY = d.originY + (int) (y * d.magSize);
                d.screen[graphX][graphY] = iterations;
            }
        }
    }

    public void mandelbrotSet() {
        Complex z = new Complex(0, 0);
        int iterations, graphX, graphY;
        for (double x = d.xLimL; x <= d.xLimU; x += d.diff) {
            iterations = -1;
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
