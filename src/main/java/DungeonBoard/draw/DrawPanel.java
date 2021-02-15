package DungeonBoard.draw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import DungeonBoard.main.Settings;
import DungeonBoard.main.Settings.Pen;
import DungeonBoard.main.Settings.Direction;
import DungeonBoard.main.Settings.DrawMode;


public class DrawPanel extends JComponent {

	public DrawPanel() {
		setDoubleBuffered(false);
		setRadius(25);
		mousePos = new Point(-100, -100);
		displayZoom = 1;
		windowPos = new Point(0, 0);
		lastWindowClick = new Point(0, 0);
		penType = Pen.CIRCLE;
		styleLock = Direction.NONE;
		drawMode = DrawMode.ANY;

		DrawPanelActionManager.addListeners(this);

		repaint();
	}

	public BufferedImage createMask() throws OutOfMemoryError {
		final BufferedImage mask = new BufferedImage(drawingLayer.getWidth(), drawingLayer.getHeight(),BufferedImage.TYPE_INT_ARGB);
		
		final int color = Settings.MASK.getRGB();
		for (int j = 0; j < drawingLayer.getHeight(); j++) {
			for (int i = 0; i < drawingLayer.getWidth(); i++) {
				final int dl = drawingLayer.getRGB(i, j);
				if ((dl & 0x00FFFFFF) == (Settings.CLEAR.getRGB() & 0x00FFFFFF)) mask.setRGB(i, j, 0);
				else mask.setRGB(i, j, color);
			}
		}
		return mask;
	}

	public synchronized void setImage() {
		if (Settings.PAINT_IMAGE != null) {
			drawingLayer = new BufferedImage(Settings.PAINT_IMAGE.getWidth() / Settings.PIXELS_PER_MASK,
					Settings.PAINT_IMAGE.getHeight() / Settings.PIXELS_PER_MASK, BufferedImage.TYPE_INT_ARGB);
			g2 = (Graphics2D) drawingLayer.getGraphics();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, .8f));
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			hideAll();
		}
	}

	public void setRadius(final int value) {
		radius = value;
		diameter = radius * 2;
		repaint();
	}

	public void togglePen() {
		penType = Pen.values()[(penType.ordinal() + 1) % Pen.values().length];
		repaint();
	}

	public void toggleStyle() {
		styleLock = Direction.values()[(styleLock.ordinal() + 1) % Direction.values().length];
	}

	public void toggleDrawMode() {
		drawMode = DrawMode.values()[(drawMode.ordinal() + 1) % DrawMode.values().length];
		if (g2 != null) {
			switch (drawMode) {
			case ANY:
				break;
			case VISIBLE:
				g2.setPaint(Settings.CLEAR);
				canDraw = true;
				break;
			case INVISIBLE:
				g2.setPaint(Settings.OPAQUE);
				canDraw = true;
				break;
			case WINDOW:
				canDraw = false;
				break;
			}
		}
	}

	public void setImageLoading(final boolean b) {
		loading = b;
		repaint();
	}

	Point getWindowPos() {
		return new Point((int) (windowPos.x / displayZoom), (int) (windowPos.y / displayZoom));
	}

	public boolean hasImage() {
		return drawingLayer != null;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D g2d = (Graphics2D) g;
		var test = this.getHeight();
		if (loading) {
			g2d.drawString("Loading...", controlSize.width / 2, controlSize.height / 2);
		} else if (Settings.PAINT_CONTROL_IMAGE != null) {
			g2d.drawImage(Settings.PAINT_CONTROL_IMAGE, 0, 0, controlSize.width, controlSize.height, null);
			g2d.drawImage(drawingLayer, 0, 0, controlSize.width, controlSize.height, null);
			g2d.setColor(Settings.PINK);
			switch (penType) {
			case CIRCLE:
				g2d.drawOval(mousePos.x - radius, mousePos.y - radius, diameter, diameter);
				break;
			case SQUARE:
				g2d.drawRect(mousePos.x - radius, mousePos.y - radius, diameter, diameter);
				break;
			case RECT:
				if (dragging) {
					g2d.drawRect(Math.min(mousePos.x, startOfClick.x), Math.min(mousePos.y, startOfClick.y),
							Math.abs(mousePos.x - startOfClick.x), Math.abs(mousePos.y - startOfClick.y));
				}
				g2d.drawLine(mousePos.x, mousePos.y - 10, mousePos.x, mousePos.y + 10);
				g2d.drawLine(mousePos.x - 10, mousePos.y, mousePos.x + 10, mousePos.y);
				break;
			}
			if(Settings.PAINT_IMAGE != null) drawPlayerView(g2d);
		} else if (controlSize != null) {
			g2d.drawString("No image loaded", controlSize.width / 2, controlSize.height / 2);
		}
	}

	private void drawPlayerView(final Graphics2D g2d) {
		final int w = (int) (Settings.DISPLAY_SIZE.width * displayZoom * controlSize.width / Settings.PAINT_IMAGE.getWidth());
		final int h = (int) (Settings.DISPLAY_SIZE.height * displayZoom * controlSize.height / Settings.PAINT_IMAGE.getHeight());
		int x, y;

		if (w > controlSize.width) x = -(w - controlSize.width) / 2; 
			else x = windowPos.x * controlSize.width / Settings.PAINT_IMAGE.getWidth();
		if (h > controlSize.height) y = -(h - controlSize.height) / 2;
			else y = windowPos.y * controlSize.height / Settings.PAINT_IMAGE.getHeight();

		g2d.drawRect(x, y, w, h);
		g2d.drawLine(x, y, x + w, y + h);
		g2d.drawLine(x + w, y, x, y + h);
		g2d.setColor(Settings.PINK_CLEAR);
		g2d.fillRect(x, y, w, h);
	}

	private void fillAll(final Color c) {
		if (g2 != null) {
			g2.setPaint(c);
			g2.fillRect(0, 0, drawingLayer.getWidth(), drawingLayer.getHeight());
			repaint();
		}
	}
	
	public void hideAll() { fillAll(Settings.OPAQUE); }
	
	public void showAll() { fillAll(Settings.CLEAR); }

	public int getPen() { return penType.ordinal(); }
	
	public int getStyle() { return styleLock.ordinal(); }

	public int getDrawMode() { return drawMode.ordinal(); }

	private static final long serialVersionUID = 1L;
	Pen penType;
	Graphics2D g2;
	Dimension controlSize;
	double displayZoom;
	Direction styleLock;
	DrawMode drawMode;
	BufferedImage drawingLayer;
	int radius, diameter;
	boolean canDraw, loading, dragging;
	Point lastWindowClick, windowPos, startOfClick, lastP, mousePos;
}