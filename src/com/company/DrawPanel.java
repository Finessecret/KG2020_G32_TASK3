package com.company;

import com.company.line.DDALineDrawer;
import com.company.line.Line;
import com.company.line.LineDrawer;
import com.company.pixel.BufferedImagePixelDrawer;
import com.company.pixel.PixelDrawer;
import com.company.polygon.PolygonDrawerLine;
import com.company.polygon.RegularPolygon;
import com.company.polygon.drawer.RegularPolygonDrawer;
import com.company.screen_conversion.RealPoint;
import com.company.screen_conversion.ScreenConverter;
import com.company.screen_conversion.ScreenPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private ScreenConverter sc = new ScreenConverter(-2, 2, 4, 4, 800, 600);
    private Line xAxis = new Line(-1, 0, 1, 0);
    private Line yAxis = new Line(0, -1, 0, 1);

    private TextField radiusTextField = new TextField("1");
    private TextField countSidesTextField = new TextField("3");
    private TextField angleTextField = new TextField("90");
    private Button drawPolygonButton = new Button("Рисовать");

    private ArrayList<Line> allLines = new ArrayList<>();
    private List<RegularPolygon> allPolygons = new ArrayList<>();
    private RealPoint centerOfNewPolygon = null;
    private Line currentNewLine = null;

    public DrawPanel() {
        this.add(radiusTextField);
        this.add(countSidesTextField);
        this.add(angleTextField);
        this.add(drawPolygonButton);
        /*drawPolygonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (centerOfNewPolygon != null && )
            }
        });*/
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseWheelListener(this);
    }

    @Override
    public void paint(Graphics g) {
        sc.setScreenWidth(getWidth());
        sc.setScreenHeight(getHeight());

        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics gr = bi.createGraphics();
        gr.setColor(new Color(255, 255, 255));
        gr.fillRect(0, 0, getWidth(), getHeight());
        gr.dispose();

        PixelDrawer pd = (PixelDrawer) new BufferedImagePixelDrawer(bi);

        LineDrawer ld = new DDALineDrawer(pd);


        drawLine(ld, xAxis);
        drawLine(ld, yAxis);

        for (Line l : allLines) {
            drawLine(ld, l);
        }
        if (currentNewLine != null) {
            drawLine(ld, currentNewLine);
        }
        RegularPolygonDrawer rpd = new PolygonDrawerLine(pd);
        rpd.setLineDrawer(ld);
        //RegularPolygon pol = new RegularPolygon(new RealPoint(0, 0), 1, 5, 1);
        drawAllPolygons(rpd);
        g.drawImage(bi, 0, 0, null);
    }

    private RegularPolygon p = null;

    public void drawAllPolygons(RegularPolygonDrawer rpd) {
        drawPolygon(rpd);
        if (p != null) drawRegularPolygon(p, rpd);
    }

    private void drawLine(LineDrawer ld, Line l) {
        ld.drawLine(sc.rts(l.getP1()), sc.rts(l.getP2()));

    }

    public void drawPolygon(RegularPolygonDrawer rpd) {
        drawPolygonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double radius = Double.parseDouble(radiusTextField.getText());
                int count = Integer.parseInt(countSidesTextField.getText());
                double angleGradus = Double.parseDouble(angleTextField.getText());
                if (centerOfNewPolygon != null) {
                    p = new RegularPolygon(centerOfNewPolygon, radius, count, angleGradus * Math.PI / 180);
                    centerOfNewPolygon = null;
                } else {
                    System.out.println("null");
                }
            }
        });
    }

    private void drawRegularPolygon(RegularPolygon pol, RegularPolygonDrawer rpd) {
        RealPoint[] rPoints = rpd.getVertexes(pol);
        ScreenPoint[] sPoints = new ScreenPoint[rPoints.length];
        for (int i = 0; i < sPoints.length; i++) {
            sPoints[i] = sc.rts(rPoints[i]);
        }
        rpd.drawPolygon(sPoints);
    }

    private ScreenPoint lastPosition = null;

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

        ScreenPoint currentPosition = new ScreenPoint(mouseEvent.getX(), mouseEvent.getY());
        if (lastPosition != null) {
            ScreenPoint deltaScreen = new ScreenPoint(currentPosition.getX() - lastPosition.getX(), currentPosition.getY() - lastPosition.getY());

            RealPoint deltaReal = sc.s2r(deltaScreen);
            RealPoint zeroReal = sc.s2r(new ScreenPoint(0, 0));
            RealPoint vector = new RealPoint(deltaReal.getX() - zeroReal.getX(), deltaReal.getY() - zeroReal.getY());

            sc.setCornerX(sc.getCornerX() - vector.getX());
            sc.setCornerY(sc.getCornerY() - vector.getY());
            lastPosition = currentPosition;
        }
        if (currentNewLine != null) {
            currentNewLine.setP2(sc.s2r(currentPosition));
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON1)
            centerOfNewPolygon = sc.s2r(new ScreenPoint(mouseEvent.getX(), mouseEvent.getY()));
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
            lastPosition = new ScreenPoint(mouseEvent.getX(), mouseEvent.getY());
        } else if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            currentNewLine = new Line(sc.s2r(new ScreenPoint(mouseEvent.getX(), mouseEvent.getY())), sc.s2r(new ScreenPoint(mouseEvent.getX(), mouseEvent.getY())));
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
            lastPosition = null;
        } else if (mouseEvent.getButton() == mouseEvent.BUTTON1) {
            allLines.add(currentNewLine);
            currentNewLine = null;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        int clicks = mouseWheelEvent.getWheelRotation();
        double scale = 1;
        double coef = clicks < 0 ? 1.1 : 0.9;
        for (int i = 0; i < Math.abs(clicks); i++) {
            scale *= coef;
        }
        sc.setRealWidth(sc.getRealWidth() * scale);
        sc.setRealHeight(sc.getRealHeight() * scale);
        repaint();
    }
}
