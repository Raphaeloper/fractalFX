package sample;

public class Complex {
    double a, b, r, rad;


    public Complex(double a, double b) {
        this.a = a;
        this.b = b;
        r = abs();
        rad = Math.atan(b / a);
    }

    public Complex(Complex c) {
        this.a = c.a;
        this.b = c.b;
        this.r = c.r;
        this.rad = c.rad;
    }

    public void update(double a, double b) {
        this.a = a;
        this.b = b;
        r = abs();
        rad = Math.atan(b / a);
    }

    public void update(double rad) {
        this.rad = rad;
        a = r * Math.cos(rad);
        b = r * Math.sin(rad);
    }

    public double abs() {
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    public Complex square() {
        if (a != 0 && b != 0) {
            r = Math.pow(r, 2);
            rad *= 2;
            a = r * Math.cos(rad);
            b = r * Math.sin(rad);
        }
        return this;
    }

    public void add(Complex c) {
        this.a += c.a;
        this.b += c.b;
        r = abs();
        rad = Math.atan(b / a);
    }

    public void nullify() {
        this.a = 0;
        this.b = 0;
        this.r = 0;
        this.rad = 0;
    }

    @Override
    public String toString() {
        return "Complex{" +
                "a=" + String.format("%.4f", a) +
                ", b=" + String.format("%.4f", b) +
                ", r=" + String.format("%.4f", r) +
                ", rad=" + String.format("%.4f", rad) +
                '}';
    }
}
