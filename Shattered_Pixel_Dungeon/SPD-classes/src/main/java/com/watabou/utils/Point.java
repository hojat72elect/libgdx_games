

package com.watabou.utils;

public class Point {

    public int x;
    public int y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Point set(Point p) {
        x = p.x;
        y = p.y;
        return this;
    }

    public Point clone() {
        return new Point(this);
    }

    public Point scale(float f) {
        this.x *= f;
        this.y *= f;
        return this;
    }

    public Point offset(int dx, int dy) {
        x += dx;
        y += dy;
        return this;
    }

    public Point offset(Point d) {
        x += d.x;
        y += d.y;
        return this;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static float distance(Point a, Point b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point p = (Point) obj;
            return p.x == x && p.y == y;
        } else {
            return false;
        }
    }
}
