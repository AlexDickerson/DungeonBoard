package DungeonBoard.control;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import DungeonBoard.main.Main;
import DungeonBoard.main.Settings;
import DungeonBoard.draw.DrawPanel;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.scale.AWTUtil;
import java.awt.Point;

public class FileLoader {
	
	public FileLoader(DrawPanel draw, ControlPaint controlJPanel) {
        drawPanel = draw;
        controlPaint = controlJPanel;
	}

	private BufferedImage rotateImage90Degrees(final BufferedImage img) {
		final int w = img.getWidth();
		final int h = img.getHeight();

		final BufferedImage rotated = new BufferedImage(h, w, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2d = rotated.createGraphics();
		final AffineTransform at = new AffineTransform();

		at.translate((h - w) / 2, (w - h) / 2);
		at.rotate(Math.toRadians(90.0), w/2, h/2);
		g2d.setTransform(at);
		
		g2d.drawImage(img, 0, 0, controlPaint);
		g2d.setColor(Settings.CLEAR);
		g2d.drawRect(0, 0, h, w);
		g2d.dispose();

		return rotated;
	}

	void browse(final String type) {
		final Frame fileDialogFrame = new Frame();
		final FileDialog fd = new FileDialog(fileDialogFrame, "Choose a file", FileDialog.LOAD);
		if (type == "token")
			fd.setMultipleMode(true);
		fd.setVisible(true);
		if (type == "map") {
			final String filename = fd.getFile();
			if (filename != null){
				if (URLConnection.guessContentTypeFromName(fd.getFile()).startsWith("video")) {
					final String path = fd.getDirectory() + fd.getFile();
					BufferedImage frame = null;
					
					try{
						frame = AWTUtil.toBufferedImage(FrameGrab.getFrameFromFile(new File(path), 1));
						setFile(ImageIO.read(this.getClass().getResource("/clear.png")), frame);
					} catch (IOException | JCodecException e) {
						System.out.println(e.getMessage());
					}
					Main.playVideo(path);
				} else {
					final File maskFile = fd.getFiles()[0];
					Main.displayImage();
					setFile(maskFile);
				}
			}
		}

		if (type == "token") {
			if (fd.getFile() != null) {
				final File files[] = fd.getFiles();
				for (final File img : files)
					addFile(img);
			}
		}
	}

	void setFile(final File file) {
		drawPanel.setImageLoading(true);
		final Thread fileLoadingThread = new Thread("fileLoadingThread") {
			public void run() {
				try {
					Settings.PAINT_IMAGE = null;
					ImageIO.setUseCache(false);
					Settings.PAINT_IMAGE = ImageIO.read(file);
					Main.setWindowPositions(new Point(0,0));
					if (Settings.PAINT_IMAGE.getHeight() > Settings.PAINT_IMAGE.getWidth()) {
						Settings.PAINT_IMAGE = rotateImage90Degrees(Settings.PAINT_IMAGE);
					}
					Settings.PAINT_CONTROL_IMAGE = Settings.PAINT_IMAGE;
					if (Settings.PAINT_IMAGE != null) {
						drawPanel.setImage();
						Main.setMask();
					}
				} catch (IOException | OutOfMemoryError error) {
					Main.resetImages();
					Settings.PAINT_IMAGE = null;
					Main.showError("Cannot load Image, file is probably too large", error);
				}
				Main.repaintDisplays();
				drawPanel.setImageLoading(false);
				Settings.maxZoom = (double) Settings.PAINT_IMAGE.getHeight() / Settings.DISPLAY_SIZE.height;
			}
		};
		fileLoadingThread.start();
	}

	void setFile(BufferedImage controlImage, BufferedImage image) {
		drawPanel.setImageLoading(true);
		final Thread fileLoadingThread = new Thread("fileLoadingThread") {
			public void run() {
				try {
					Settings.PAINT_IMAGE = null;
					ImageIO.setUseCache(false);
					Settings.PAINT_IMAGE = controlImage;
					Main.setWindowPositions(new Point(0,0));
					if (Settings.PAINT_IMAGE.getHeight() > Settings.PAINT_IMAGE.getWidth()) {
						Settings.PAINT_IMAGE = rotateImage90Degrees(Settings.PAINT_IMAGE);
					}
					Settings.PAINT_CONTROL_IMAGE = image;
					if (Settings.PAINT_IMAGE != null) {
						drawPanel.setImage();
						Main.setMask();
					}
				} catch (OutOfMemoryError error) {
					Main.resetImages();
					Settings.PAINT_IMAGE = null;
					System.out.println(error.getMessage());
					Main.showError("Cannot load Image, file is probably too large", error);
				}
				Main.repaintDisplays();
				drawPanel.setImageLoading(false);
				Settings.maxZoom = (double) Settings.PAINT_IMAGE.getHeight() / Settings.DISPLAY_SIZE.height;
			}
		};
		fileLoadingThread.start();
	}

	void addFile(final File file) {
		drawPanel.setImageLoading(true);
		final Thread fileLoadingThread = new Thread("fileLoadingThread") {
			public void run() {
				try {
					final BufferedImage img = ImageIO.read(file);
					String name = file.getName().replaceFirst("[.][^.]+$", "");;
					Main.tokenList.addToken(img, name);
				} catch (IOException | OutOfMemoryError error) {
					Main.showError("Cannot load Image, file is probably too large", error);
				}
				Main.repaintDisplays();
				drawPanel.setImageLoading(false);
			}
		};
		fileLoadingThread.start();
	}

    private DrawPanel drawPanel;
    private ControlPaint controlPaint;
}