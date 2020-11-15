package com.company.polygon.drawer;

import com.company.polygon.PolygonDrawer;
import com.company.polygon.RegularPolygon;
import com.company.screen_conversion.RealPoint;

public interface RegularPolygonDrawer extends PolygonDrawer {
    RealPoint[] getVertexes(RegularPolygon polygon);
}
