package DungeonBoard.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Icon;

public class Settings {

	public enum DrawMode { ANY, VISIBLE, INVISIBLE, WINDOW }

	public enum Direction { NONE, VERTICAL, HORIZONTAL }

	public enum Pen { CIRCLE, SQUARE, RECT }

	public static int screenSize = 32;

	public static int displayGrid = 0;

	public static int displayTokens = 0;
	
	public static final String NAME = "Dungeon Board";

	public static final ImageIcon ICON = load("icon.gif");

	public static final ImageIcon DRAW_STYLE[] = {
			load("squigle.gif"),
			load("vertical.gif"),
			load("horizontal.gif")
	};
	
	public static final ImageIcon DRAW_MODE[] = {
			load("mouse.gif"),
			load("visible.gif"),
			load("invisible.gif"),
			load("move.gif")
	};
	
	public static final ImageIcon PEN_TYPE[] = {
			load("circle.gif"),
			load("square.gif"),
			load("rect.gif")
	};

	public static double maxZoom = 0;

	public static Integer playingVideo = 0;
	
	public static final BufferedImage BLANK_CURSOR = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);

	public static BufferedImage PAINT_IMAGE;

	public static BufferedImage PAINT_CONTROL_IMAGE;

	public static final Dimension CONTROL_SIZE = new Dimension(1000, 600);

	public static final Dimension INITATIVE_SIZE = new Dimension(230, CONTROL_SIZE.height);

	public static final Dimension INITATIVE_CHARACTER_SIZE = new Dimension(200, 50);

	public static Dimension DISPLAY_SIZE;
	
	public static final Color CLEAR = new Color(0, 0, 0, 0);

	public static final Color MASK = new Color(0, 0, 0, 255);
	
	public static final Color OPAQUE = new Color(42, 49, 60, 255);

	public static final Color PINK = new Color(255, 0, 255);

	public static final Color PINK_CLEAR = new Color(255, 0, 255, 0);

	public static final Color TEXT = new Color(126, 132, 145, 255);
	
	public static final Color TEXT_BACKGROUND = new Color(61, 66, 75, 255);
	
	
	/**
	 * number of pixels on the Paint image that are being covered by a single pixel on the mask.<br>
	 * - higher number means the shadows will be more blocky<br>
	 * - lower number means the shadows will be more fine, but will use more memory and CPU time
	 */
	public static final int PIXELS_PER_MASK = 3;
	
	public static ImageIcon load(final String res) {
		return new ImageIcon(ClassLoader.getSystemResource(res));
	}

	public static JButton createButton(final String label) {
		final JButton button = new JButton(label);
		button.setFocusPainted(false);
		button.setRolloverEnabled(false);
		button.setFocusable(false);
		return button;
	}

	public static JButton createButton(final ImageIcon imageIcon) {
		final JButton button = new JButton(imageIcon);
		button.setFocusPainted(false);
		button.setRolloverEnabled(false);
		button.setFocusable(false);
		return button;
	}

	public static JButton createButton(final Icon imageIcon) {
		final JButton button = new JButton(imageIcon);
		button.setFocusPainted(false);
		button.setRolloverEnabled(false);
		button.setFocusable(false);
		return button;
	}
}