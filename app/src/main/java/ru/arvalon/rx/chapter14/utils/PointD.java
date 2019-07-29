package ru.arvalon.rx.chapter14.utils;

public class PointD {

    final public double x;
    final public double y;

    public PointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "PointD(" + x + ", " + y + ")";
    }
}
