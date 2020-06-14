package DungeonBoard.display;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import DungeonBoard.main.Main;

class DisplayPaintActionManager {

	static void addListeners(final DisplayPaint displayPaintComponent) {
		displayPaintComponent.addMouseListener(new MouseAdapter() {
			public void mousePressed(final MouseEvent e) {
				tokenIndex = 0;
				if (e.isShiftDown()) {
					for(int i = 0; i < Main.tokenList.tokenSize(); i++){
						final var image = Main.tokenList.getToken(i);
						if (e.getX() > image.getXPos() && e.getY() > image.getYPos()
								&& e.getX() < image.getXPos() + image.getWidth()
								&& e.getY() < image.getYPos() + image.getHeight()) {
							Main.tokenList.setToken(i,image.getImage(), image.getXPos(), image.getYPos(), oneInchSize(), oneInchSize(), image.getName(), image.getStatusList());
							Main.repaintDisplays();
						}
					}
				} else if (e.getButton() == 3) {
					for(int i = 0; i < Main.tokenList.tokenSize(); i++){
						final var image = Main.tokenList.getToken(i);
						if (e.getX() > image.getXPos() && e.getY() > image.getYPos()
								&& e.getX() < image.getXPos() + image.getWidth()
								&& e.getY() < image.getYPos() + image.getHeight()) {
							StatusMenu.createStatusMenu(i).show(e.getComponent(), e.getX(), e.getY());
						}
					}
				} else {
					for(int i = 0; i < Main.tokenList.tokenSize(); i++){
						final var image = Main.tokenList.getToken(i);
						if (e.getX() > image.getXPos() && e.getY() > image.getYPos()
								&& e.getX() < image.getXPos() + image.getWidth()
								&& e.getY() < image.getYPos() + image.getHeight()) {
							insideToken = 1;
							previousX = e.getX();
							previousY = e.getY();
							tokenIndex = i;
							break;
						}
					}
				}
			}

			public void mouseReleased(final MouseEvent e) {
				if (insideToken == 1) {
					final var image = Main.tokenList.getToken(tokenIndex);
					final int newX = e.getX() - previousX;
					final int newY = e.getY() - previousY;
					Main.tokenList.setToken(tokenIndex, image.getImage(), image.getXPos() + newX, image.getYPos() + newY, image.getWidth(), image.getHeight(), image.getName(), image.getStatusList());

					Main.repaintDisplays();
					insideToken = 0;
					tokenIndex = 0;
				}
			}
		});

		displayPaintComponent.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(final MouseWheelEvent e) {
				for(int i = 0; i < Main.tokenList.tokenSize(); i++){
					final var image = Main.tokenList.getToken(i);
					if (e.getX() > image.getXPos() && e.getY() > image.getYPos()
							&& e.getX() < image.getXPos() + image.getWidth()
							&& e.getY() < image.getYPos() + image.getHeight()) {
						if (e.getWheelRotation() < 0) {
							Main.tokenList.setToken(i,image.getImage(), image.getXPos(),image.getYPos(),
								(int) Math.round(image.getWidth()+ (image.getWidth() * .05)),
								(int) Math.round(image.getHeight()+ (image.getHeight() * .05)),
								image.getName(), image.getStatusList());
						} else {
							Main.tokenList.setToken(i, image.getImage(), image.getXPos(), image.getYPos(),
								(int) Math.round(image.getWidth()- (image.getWidth() * .05)),
								(int) Math.round(image.getHeight()- (image.getHeight() * .05)),
								image.getName(), image.getStatusList());
						}
						Main.repaintDisplays();
					}
				}
			}
		});

		displayPaintComponent.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(final MouseEvent e) {
				if (insideToken == 1) {
					final var image = Main.tokenList.getToken(tokenIndex);
					final int newX = e.getX() - previousX;
					final int newY = e.getY() - previousY;
					Main.tokenList.setToken(tokenIndex, image.getImage(), image.getXPos() + newX, image.getYPos() + newY, image.getWidth(), image.getHeight(), image.getName(), image.getStatusList());

					previousX = e.getX();
					previousY = e.getY();

					Main.repaintDisplays();
				}
			}
		});
	}

	private static Integer oneInchSize() {
		int dpi = 96;
		return dpi;
	}

	private static int insideToken = 0, tokenIndex = 0, previousX = 0, previousY = 0;
 }