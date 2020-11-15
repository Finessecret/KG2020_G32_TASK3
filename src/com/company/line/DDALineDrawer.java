package com.company.line;

import com.company.pixel.PixelDrawer;
import com.company.screen_conversion.ScreenPoint;

import java.awt.*;

public class DDALineDrawer implements LineDrawer {
    private final PixelDrawer pd;

    public DDALineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawLine(ScreenPoint p1, ScreenPoint p2) {
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;
        if (Math.abs(dx) > Math.abs(dy)) {
            double k = dy / dx;
            if (x1 > x2) {
                int tmp = x1;
                x1 = x2;
                x2 = tmp;
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            for (int j = x1; j <= x2; j++) {
                double i = k * (j - x1) + y1;
                pd.drawPixel(j, (int) i, new Color(20,110,100));
            }
        } else {
            double kObr = dx / dy;
            if (y1 > y2) {
                int tmp = x1;
                x1 = x2;
                x2 = tmp;
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            for (int i = y1; i <= y2; i++) {
                double j = kObr * (i - y1) + x1;
                pd.drawPixel((int) j, i, new Color(0,155,0));
            }
        }
    }

}
