package sample;

public class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static double getY(double slope, double x) {
        return slope * x;
    }

    public static double getX(double slope, double y) {
        return slope / y;
    }

    public double getSlope(Point p) {
        return (p.y - y) / (p.x - x);
    }

    public void setMid(Point p) {
        x = (x + p.x) / 2;
        y = (y + p.y) / 2;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
