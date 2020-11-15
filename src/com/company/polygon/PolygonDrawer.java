package com.company.polygon;

import com.company.line.LineDrawer;
import com.company.screen_conversion.ScreenPoint;

import java.awt.*;

public interface PolygonDrawer {
    void drawPolygon(ScreenPoint[] screenPoints);
    void setLineDrawer(LineDrawer ld);
    LineDrawer getLineDrawer();
    Color getColor();
    void setColor(Color c);
}

