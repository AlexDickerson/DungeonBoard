package DungeonBoard.main;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.HeadlessException;
import java.awt.GraphicsEnvironment;

public class Screen {

	public Screen(final GraphicsDevice graphicsDevice) {
		rectangle = graphicsDevice.getDefaultConfiguration().getBounds();
		name = graphicsDevice.getIDstring();
	}

	@Override
	public String toString() {
		return name + "  " + rectangle.width + "x" + rectangle.height;
	}

	public static Screen[] getScreens() throws HeadlessException {
		final GraphicsDevice[] graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		final Screen[] screens = new Screen[graphicsDevice.length];
		for (int i = 0; i < graphicsDevice.length; i++) {
			screens[i] = new Screen(graphicsDevice[i]);
		}
		return screens;
	}

	public Dimension getSize() { return rectangle.getSize(); }
	public Rectangle getRectangle() { return rectangle; }

	private final Rectangle rectangle;
	private final String name;
}