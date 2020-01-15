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
import java.awt.Font;
import java.awt.font.GlyphVector;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
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

				paintLabels(g2d, i);
				paintStatus(g2d, i);
			} catch (final NullPointerException e) {
			}
		}
	}

	private void paintLabels(final Graphics2D g2d2, final int i) {
		Graphics2D g2d = (Graphics2D) g2d2.create();
		final var token = Main.tokenList.getToken(i);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(new Font("Helvetica", Font.BOLD, 18));

		String name = token.getName();

		FontRenderContext frc = g2d.getFontRenderContext();
		g2d.translate(token.getXPos() + token.getWidth()/2, token.getYPos() + token.getHeight()/2);

		Shape ellipse = getEllipseFromCenter(0, 0, token.getWidth()+30, token.getHeight()+30);
		g2d.setStroke(new BasicStroke(30));
		g2d.setColor(Settings.TEXT_BACKGROUND);
		g2d.draw(ellipse);

		Shape ellipse2 = getEllipseFromCenter(0, 0, token.getWidth()+50, token.getHeight()+50);
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Settings.TEXT);
		g2d.draw(ellipse2);

		Shape ellipse3 = getEllipseFromCenter(0, 0, token.getWidth(), token.getHeight());
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Settings.TEXT);
		g2d.draw(ellipse3);

		GlyphVector gv = g2d.getFont().createGlyphVector(frc, name);
		int length = gv.getNumGlyphs();
		for (int j = 0; j < length; j++) {
			gv.setGlyphPosition(j, new Point(0,0));
			AffineTransform at;
			double degrees = 10;
			int ellipseOffset = 15;
			if(j < ((length-1)/2)){
				double ePX = (token.getWidth()/2 + ellipseOffset) * Math.cos(Math.toRadians(-90 - (degrees * ((length/2 - j)))));
				double ePY = (token.getHeight()/2 + ellipseOffset) * Math.sin(Math.toRadians(-90 - (degrees * ((length/2 - j)))));
				var glyph = gv.getGlyphOutline(j);
				at = AffineTransform.getTranslateInstance(ePX - glyph.getBounds2D().getWidth()/2,ePY + glyph.getBounds2D().getHeight()/2);
				at.rotate(-Math.toRadians(degrees * (length/2 - j)), glyph.getBounds2D().getWidth()/2, -glyph.getBounds2D().getHeight()/2);
			} else if(j == (length/2)){
				double ePX = (token.getWidth()/2 + ellipseOffset) * Math.cos(Math.toRadians(-90));
				double ePY = (token.getHeight()/2 + ellipseOffset) * Math.sin(Math.toRadians(-90));
				var glyph = gv.getGlyphOutline(j);
				at = AffineTransform.getTranslateInstance(ePX - glyph.getBounds2D().getWidth()/2,ePY + glyph.getBounds2D().getHeight()/2);
			}
			else{
				double ePX = (token.getWidth()/2 + ellipseOffset) * Math.cos(Math.toRadians(-90 + ((j - length/2) * degrees)));
				double ePY = (token.getHeight()/2 + ellipseOffset) * Math.sin(Math.toRadians(-90 + ((j - length/2) * degrees)));
				var glyph = gv.getGlyphOutline(j);
				at = AffineTransform.getTranslateInstance(ePX - glyph.getBounds2D().getWidth()/2,ePY + glyph.getBounds2D().getHeight()/2);
				at.rotate(Math.toRadians(((j - length/2) * degrees)), glyph.getBounds2D().getWidth()/2, -glyph.getBounds2D().getHeight()/2);
			}
		
			Shape glyph = gv.getGlyphOutline(j);
			Shape transformedGlyph = at.createTransformedShape(glyph);
			g2d.setColor(Color.WHITE);
			g2d.fill(transformedGlyph);
			//g2d.setColor(Color.BLACK);
			//g2d.setStroke(new BasicStroke(1));
			//g2d.draw(transformedGlyph);
		}
	}

	private void paintStatus(Graphics2D g2d, Integer i){
		final var token = Main.tokenList.getToken(i);
		try {
			if (!token.getName().isEmpty())
				for (final String status : token.getStatusList()) 
					g2d.drawImage(ImageIO.read(this.getClass().getResource("/" + status + ".png")), 
						token.getXPos() + 10, token.getYPos() + 90, token.getWidth() - 15, token.getHeight() - 15, null);
		} catch (final IOException e) { }
	}

	private Ellipse2D getEllipseFromCenter(double x, double y, double width, double height)
	{
		double newX = x - width / 2.0;
		double newY = y - height / 2.0;

		Ellipse2D ellipse = new Ellipse2D.Double(newX, newY, width, height);

		return ellipse;
	}

	private void paintGrid(final Graphics2D g) {
		g.setStroke(new BasicStroke(2));
		g.setColor(Settings.TEXT_BACKGROUND);

		final int width = getSize().width;
		final int height = getSize().height;
		int dpi = 96;
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
		System.out.println(windowPos.x + " " + windowPos.y);
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