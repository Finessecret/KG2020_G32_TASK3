package com.company.polygon;

import com.company.pixel.PixelDrawer;
import com.company.line.LineDrawer;
import com.company.polygon.drawer.RegularPolygonDrawer;
import com.company.screen_conversion.RealPoint;
import com.company.screen_conversion.ScreenPoint;

import java.awt.*;

   public class PolygonDrawerLine implements PolygonDrawer, RegularPolygonDrawer {

        private PixelDrawer pd;
        private Color c;
        private LineDrawer ld;

        public PolygonDrawerLine(PixelDrawer pd) {
            this.pd = pd;
            this.c = Color.BLACK;
        }


        @Override
        public void drawPolygon(ScreenPoint[] screenPoints) {
            for(int i = 0; i < screenPoints.length - 1; i++){
                ld.drawLine(screenPoints[i], screenPoints[i + 1]);
            }
            ld.drawLine(screenPoints[screenPoints.length - 1], screenPoints[0]);
        }

        @Override
        public void setLineDrawer(LineDrawer ld) {
            this.ld = ld;
        }

        @Override
        public LineDrawer getLineDrawer() {
            return ld;
        }

        @Override
        public Color getColor() {
            return c;
        }

        @Override
        public void setColor(Color c) {
            this.c = c;
        }

       @Override
       public RealPoint[] getVertexes(RegularPolygon polygon) {
            int size = polygon.getSideCount();
            RealPoint[] vertexes = new RealPoint[size];
            int i = 0;
            double stepAngle = 2 * Math.PI / size;
            for (double currAngle = polygon.getAngle(); i < size; currAngle += stepAngle, i++) {
                double x = polygon.getCenter().getX() + Math.cos(currAngle) * polygon.getRadius();
                double y = polygon.getCenter().getY() + Math.sin(currAngle) * polygon.getRadius();
                vertexes[i] = new RealPoint(x, y);
            }
           return vertexes;
       }
   }
