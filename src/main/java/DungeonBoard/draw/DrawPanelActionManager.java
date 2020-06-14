package DungeonBoard.draw;

import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import DungeonBoard.main.Main;
import DungeonBoard.main.Settings;

class DrawPanelActionManager {

    static void addListeners(final DrawPanel draw) {
        draw.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(final MouseEvent e) {
                if (Settings.PAINT_IMAGE != null) {
                    if (draw.canDraw) {
                        DrawPanelWindowManager.addPoint(draw, DrawPanelWindowManager.toDrawingPoint(draw, e.getPoint()));
                    } else {
                        DrawPanelWindowManager.setWindowPos(draw, DrawPanelWindowManager.toDrawingPoint(draw, e.getPoint()));
                        Main.setWindowPositions(draw.getWindowPos());
                    }
                    draw.mousePos = e.getPoint();
                    draw.repaint();
                }
            }

            public void mouseMoved(final MouseEvent e) {
                draw.mousePos = e.getPoint();
                draw.repaint();
            }
        });

        draw.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(final MouseWheelEvent e) {
                if(e.isShiftDown()){
                    if (e.getWheelRotation() < 0) {
                        draw.setRadius(draw.radius + 3);
                    } else {
                        draw.setRadius(draw.radius - 3);
                    }
                }
                else if (e.getWheelRotation() < 0) {
                    DrawPanelWindowManager.setZoom(draw, draw.displayZoom + 0.1);
                } else {
                    DrawPanelWindowManager.setZoom(draw, draw.displayZoom - 0.1);
                }
            }
        });

        draw.addComponentListener(new ComponentListener() {
            public void componentShown(final ComponentEvent e) {
            }

            public void componentResized(final ComponentEvent e) {
                draw.controlSize = draw.getSize();
                draw.repaint();
            }

            public void componentMoved(final ComponentEvent e) {
            }

            public void componentHidden(final ComponentEvent e) {
            }
        });
        draw.repaint();

        draw.addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent e) {
                if (Settings.PAINT_IMAGE != null) {
                    draw.lastP = DrawPanelWindowManager.toDrawingPoint(draw, e.getPoint());
                    switch (draw.drawMode) {
                    case ANY:
                        if (e.getButton() == MouseEvent.BUTTON2) {
                            DrawPanelWindowManager.setWindowPos(draw, draw.lastP);
                            Main.setWindowPositions(draw.getWindowPos());
                            draw.canDraw = false;
                        } else {
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                draw.g2.setPaint(Settings.CLEAR);
                                draw.canDraw = true;
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                draw.g2.setPaint(Settings.OPAQUE);
                                draw.canDraw = true;
                            }
                            draw.startOfClick = e.getPoint();
                            draw.dragging = true;
                            DrawPanelWindowManager.addPoint(draw, draw.lastP);
                        }
                        break;
                    case INVISIBLE:
                    case VISIBLE:
                        draw.startOfClick = e.getPoint();
                        draw.dragging = true;
                        DrawPanelWindowManager.addPoint(draw, draw.lastP);
                        break;
                    case WINDOW:
                    DrawPanelWindowManager.setWindowPos(draw, draw.lastP);
                        Main.setWindowPositions(draw.getWindowPos());
                        break;
                    }
                    draw.repaint();
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (Settings.PAINT_IMAGE != null && draw.canDraw) {
                    switch (draw.penType) {
                    case RECT:
                        final Point p = DrawPanelWindowManager.toDrawingPoint(draw, e.getPoint());
                        final Point p2 = DrawPanelWindowManager.toDrawingPoint(draw, draw.startOfClick);
                        draw.g2.fillRect(Math.min(p.x, p2.x), Math.min(p.y, p2.y), Math.abs(p.x - p2.x),
                                Math.abs(p.y - p2.y));
                        break;
                    default:
                        break;
                    }
                draw.dragging = false;
                draw.repaint();
                Main.setMask();
                }
            }
        });
    }
 }