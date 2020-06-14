package DungeonBoard.main;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.json.ParseException;
import java.awt.Point;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	public static void main(final String[] args) {

		try {
			final FlatLaf test = IntelliJTheme.createLaf(Main.class.getResourceAsStream("/one_dark.theme.json"));
			UIManager.setLookAndFeel(test);
		} catch (final UnsupportedLookAndFeelException | IOException | ParseException e) {
			showError("Error - Changing look and feel", e);
		}

		tokenList = new Tokens();		
		displayStaticAndVideoWindow = new DisplayVideoCombo();
		displayWindows = new DisplayWindows(displayStaticAndVideoWindow);
	}

	public static void playVideo(String path){ 
		displayWindows.setVideoWindowVisible(); 
		displayStaticAndVideoWindow.playVideo(path);
	}

	public static void displayImage(){ 
		displayWindows.displayImage(); }
	public static void showError(final String message, final Throwable error) { 
		displayWindows.showError(message, error); }
	public static void swapControlGMDisplay(){ 
		displayStaticAndVideoWindow.swapControlGMDisplay();}
	public static void repaintDisplays(){ 
		displayStaticAndVideoWindow.repaintDisplays(); }
	public static void resetImages(){ 
		displayStaticAndVideoWindow.resetImages(); }
	public static void setMask(){ 
		displayStaticAndVideoWindow.setMask(); }
	public static void setWindows(double zoom, Point position){ 
		displayStaticAndVideoWindow.setWindows(zoom, position); }
	public static void setWindowPositions(Point position){ 
		displayStaticAndVideoWindow.setWindowPositions(position); }

	private static DisplayVideoCombo displayStaticAndVideoWindow;
	private static DisplayWindows displayWindows;
	public static Tokens tokenList;		
}