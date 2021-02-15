package DungeonBoard.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;
import DungeonBoard.main.Main;
import DungeonBoard.main.Settings;
import javax.imageio.ImageIO;
import java.awt.BasicStroke;

public class DisplayPaint extends JPanel {
	private final String display;

	public DisplayPaint(String displayType) {
		display = displayType;
		windowPos = new Point(0, 0);
		scale = 1;
		setOpaque(false);
		setVisible(true);
		DisplayPaintActionManager.addListeners(this);
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		final Graphics2D g2d = ((Graphics2D) g);

		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		if(Settings.playingVideo == 0) g2d.setColor(Color.BLACK);
		else g2d.setColor(Settings.CLEAR);
		
		g2d.fillRect(0, 0, Settings.DISPLAY_SIZE.width, Settings.DISPLAY_SIZE.height);

		if (Settings.PAINT_IMAGE != null && mask != null && imageSize != null && Settings.PAINT_CONTROL_IMAGE != null) {
			if (display == "player") g2d.drawImage(Settings.PAINT_IMAGE, -windowPos.x, -windowPos.y, imageSize.width, imageSize.height, null);
			else g2d.drawImage(Settings.PAINT_CONTROL_IMAGE, -windowPos.x, -windowPos.y, imageSize.width, imageSize.height, null);
			if (Settings.displayGrid == 1)
				paintGrid(g2d);
			if (Main.tokenList.tokenSize() > 0 && (display == "dm" || Settings.displayTokens == 1))
				paintTokens(g2d);
			g2d.drawImage(mask, -windowPos.x, -windowPos.y, imageSize.width, imageSize.height, null);
		}
		g2d.dispose();
	}

	private void paintTokens(final Graphics2D g2d) {
		for (int i = Main.tokenList.tokenSize() - 1; i >= 0; i--) {
			try {
				final var token = Main.tokenList.getToken(i);
				g2d.drawImage(token.getImage(), token.getXPos(), token.getYPos(), token.getWidth(), token.getHeight(), null);

				paintStatus(g2d, i);
			} catch (final NullPointerException e) {
			}
		}
	}

	private void paintStatus(Graphics2D g2d, Integer i){
		final var token = Main.tokenList.getToken(i);
		try {
			if (!token.getName().isEmpty())
				for (final String status : token.getStatusList()) 
					g2d.drawImage(ImageIO.read(this.getClass().getResource("/StatusIcons/" + status + ".png")), 
						token.getXPos() + 10, token.getYPos() + 90, token.getWidth() - 15, token.getHeight() - 15, null);
		} catch (final IOException e) { }
	}

	private void paintGrid(final Graphics2D g) {
		g.setStroke(new BasicStroke(2));
		g.setColor(Settings.TEXT_BACKGROUND);

		final int width = getSize().width;
		final int height = getSize().height;
		int dpi = Settings.screenDPI;
		final int rows = height/dpi;
		final int cols = width/dpi;
		for (int i = 1; i <= rows; i++)
			g.drawLine(0, i * dpi, width, i * dpi);

		for (int i = 1; i <= cols; i++)
			g.drawLine(i * dpi, 0, i * dpi, height);
	}

	public void setMask(final BufferedImage newMask) {
		mask = newMask;
		repaint();
	}

	public void setImageSize() {
		imageSize = new Dimension((int) (Settings.PAINT_IMAGE.getWidth() / scale),
				(int) (Settings.PAINT_IMAGE.getHeight() / scale));
	}

	public void setWindow(final double scale, final Point p) {
		this.scale = scale;
		if (Settings.PAINT_IMAGE != null) {
			setImageSize();
			windowPos = p;
			if (imageSize.width < Settings.DISPLAY_SIZE.width) {
				windowPos.x = (imageSize.width - Settings.DISPLAY_SIZE.width) / 2;
			}
			if (imageSize.height < Settings.DISPLAY_SIZE.height) {
				windowPos.y = (imageSize.height - Settings.DISPLAY_SIZE.height) / 2;
			}
		}
		repaint();
	}

	public void setWindowPos(final Point p) {
		windowPos = p;
		if (imageSize != null) {
			if (imageSize.width < getSize().width) {
				windowPos.x = (imageSize.width - getSize().width) / 2;
			}
			if (imageSize.height < getSize().height) {
				windowPos.y = (imageSize.height - getSize().height) / 2;
			}
		}
		repaint();
	}

	public void resetImage() {
		mask = Settings.BLANK_CURSOR;
		repaint();
	}

	private static final long serialVersionUID = 1L;
	private BufferedImage mask;
	private Dimension imageSize;
	private Point windowPos;
	private double scale;
}