package com.company.polygon;

import com.company.screen_conversion.RealPoint;

public class RegularPolygon {
    private RealPoint center;
    private double radius;
    private int sideCount;
    private double angle;

    public RegularPolygon(RealPoint center, double radius, int sideCount, double angle) {
        this.center = center;
        this.radius = radius;
        this.sideCount = sideCount;
        this.angle = angle;
    }

    public RealPoint getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public int getSideCount() {
        return sideCount;
    }

    public double getAngle() {
        return angle;
    }
}
