package DungeonBoard.draw;

import java.awt.Point;
import java.awt.Polygon;
import DungeonBoard.main.Main;
import DungeonBoard.main.Settings;

class DrawPanelWindowManager {

    static void setZoom(final DrawPanel draw, final double zoom) {

        final int h = (int) (Settings.DISPLAY_SIZE.height * draw.displayZoom * draw.controlSize.height/ Settings.PAINT_IMAGE.getHeight());
        if (h >= draw.controlSize.height) {
            draw.displayZoom = Settings.maxZoom;
            draw.displayZoom = zoom;
            setWindowPos(draw, draw.lastWindowClick);
            Main.setWindows(zoom, draw.getWindowPos());
            draw.repaint();
        }
        if (h < draw.controlSize.height + 1) {
            draw.displayZoom = zoom;
            setWindowPos(draw, draw.lastWindowClick);
            Main.setWindows(zoom, draw.getWindowPos());
            draw.repaint();
        }
    }

    /**
     * converts a point on the {@code drawingLayer} to a point on the actual image
     * @param p a point based on the placement in {@code drawingLayer}
     * @return a point based on the placement in {@code Settings.PAINT_IMAGE}
     */
    static Point toDrawingPoint(final DrawPanel draw, final Point p) {
        return new Point(p.x * draw.drawingLayer.getWidth() / draw.controlSize.width, p.y * draw.drawingLayer.getHeight() / draw.controlSize.height);
    }

    static void setWindowPos(final DrawPanel draw, final Point p) {
        draw.lastWindowClick = p;

        draw.windowPos.x = (int) (p.x * Settings.PIXELS_PER_MASK - (Settings.DISPLAY_SIZE.width * draw.displayZoom) / 2);
        draw.windowPos.y = (int) (p.y * Settings.PIXELS_PER_MASK - (Settings.DISPLAY_SIZE.height * draw.displayZoom) / 2);

        if (Settings.PAINT_IMAGE != null) {
            if (draw.windowPos.x > Settings.PAINT_IMAGE.getWidth() - Settings.DISPLAY_SIZE.width * draw.displayZoom) {
                draw.windowPos.x = (int) (Settings.PAINT_IMAGE.getWidth() - Settings.DISPLAY_SIZE.width * draw.displayZoom);
            }
            if (draw.windowPos.x < 0) {
                draw.windowPos.x = 0;
            }
            if (draw.windowPos.y > Settings.PAINT_IMAGE.getHeight() - Settings.DISPLAY_SIZE.height * draw.displayZoom) {
                draw.windowPos.y = (int) (Settings.PAINT_IMAGE.getHeight() - Settings.DISPLAY_SIZE.height * draw.displayZoom);
            }
            if (draw.windowPos.y < 0) {
                draw.windowPos.y = 0;
            }
        }
    }

    static void addPoint(final DrawPanel draw, final Point newP) {
        if (draw.g2 != null) {
            switch (draw.styleLock) {
            case HORIZONTAL:
                newP.y = draw.lastP.y;
                break;
            case VERTICAL:
                newP.x = draw.lastP.x;
                break;
            default:
                break;
            }
            final double widthMod = (double) draw.drawingLayer.getWidth() / draw.controlSize.width;
            final double heightMod = (double) draw.drawingLayer.getHeight() / draw.controlSize.height;
            final double rwidth = draw.radius * widthMod;
            final double rheight = draw.radius * heightMod;
            final int dwidth = (int) (draw.diameter * widthMod);
            final int dheight = (int) (draw.diameter * heightMod);
            switch (draw.penType) {
            case CIRCLE:
                draw.g2.fillPolygon(getCirclePolygon(newP, draw.lastP, rwidth, rheight));
                draw.g2.fillOval(newP.x - (int) rwidth, newP.y - (int) rheight, dwidth, dheight);
                break;
            case SQUARE:
                draw.g2.fillPolygon(getSquarePolygon(newP, draw.lastP, (int) rwidth, (int) rheight));
                draw.g2.fillRect(newP.x - (int) rwidth, newP.y - (int) rheight, dwidth, dheight);
                break;
            case RECT:
                break;
            }
            draw.lastP = newP;
        }
    }

    static Polygon getCirclePolygon(final Point newP, final Point oldP, final double rwidth, final double rheight) {
        final double angle = -Math.atan2(newP.getY() - oldP.getY(), newP.getX() - oldP.getX());
        final double anglePos = angle + Math.PI / 2;
        final double angleNeg = angle - Math.PI / 2;
        final int cosP = (int) (Math.cos(anglePos) * rwidth);
        final int cosN = (int) (Math.cos(angleNeg) * rwidth);
        final int sinP = (int) (Math.sin(anglePos) * rheight);
        final int sinN = (int) (Math.sin(angleNeg) * rheight);
        return new Polygon(new int[] { newP.x + cosP, newP.x + cosN, oldP.x + cosN, oldP.x + cosP },
                new int[] { newP.y - sinP, newP.y - sinN, oldP.y - sinN, oldP.y - sinP }, 4);
    }

    static Polygon getSquarePolygon(final Point newP, final Point oldP, final int rwidth, int rheight) {
        if ((newP.x > oldP.x && newP.y > oldP.y) || (newP.x < oldP.x && newP.y < oldP.y)) {
            rheight *= -1;
        }
        return new Polygon(
                new int[] {
                        newP.x - rwidth,
                        newP.x + rwidth,
                        oldP.x + rwidth,
                        oldP.x - rwidth},
                new int[] {
                        newP.y - rheight,
                        newP.y + rheight,
                        oldP.y + rheight,
                        oldP.y - rheight}, 4);
    }
 }