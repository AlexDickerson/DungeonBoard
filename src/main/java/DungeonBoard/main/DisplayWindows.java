package DungeonBoard.main;

import javax.swing.JOptionPane;
import java.awt.HeadlessException;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class DisplayWindows {
    public DisplayWindows(DisplayVideoCombo panels){
		Screen[] screens = null;
		int displayIndex = 0;
		try{ 
			screens = Screen.getScreens();
			if(screens.length > 1)
				displayIndex = JOptionPane.showOptionDialog(null, "Select Display Window", Settings.NAME, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, screens, null);
		
		} catch (final HeadlessException e) {
			System.out.println("Error - Cannot find any screens\n" + e.getMessage());
			System.exit(0);
		}
		
		Settings.DISPLAY_SIZE = screens[displayIndex].getSize();
		panels.controlPanel.setPreferredSize(new Dimension((int) Math.round(screens[0].getSize().width * 0.8), 1000));
		panels.controlPanel.setMinimumSize(new Dimension(300, 1000));

		controlWindow = new ElementWindow("Control");
		playerDisplayWindow = new ElementWindow(screens[displayIndex].getRectangle());
        playerVideoWindow = new ElementWindow(screens[displayIndex].getRectangle());
        
        playerVideoWindow.setContentPane(panels.videoPlayerPanel);
		playerDisplayWindow.add(panels.playerDisplayPanel);
		controlWindow.add(panels.controlPanel, BorderLayout.CENTER);

		synchronized (controlWindow) {
			playerDisplayWindow.setVisible(true);
			controlWindow.setVisible(true);
			playerVideoWindow.setVisible(false);
		}
	}
	

    public void setVideoWindowVisible(){
		playerVideoWindow.setVisible(true);
		Settings.playingVideo = 1;
		playerDisplayWindow.toFront();
    }
    
    public void displayImage(){
		Settings.playingVideo = 0;
		playerVideoWindow.setVisible(false);
		playerDisplayWindow.setVisible(true);
    }
    
	public void showError(final String message, final Throwable error) { JOptionPane.showMessageDialog(controlWindow, message + "\n" + error.getMessage()); }
		
    private ElementWindow controlWindow;
    private ElementWindow playerDisplayWindow;
	private ElementWindow playerVideoWindow;
	
	private class ElementWindow extends JFrame {
		
		public ElementWindow(final String s) {
			setIconImage(Settings.ICON.getImage());
	
			if (s == "Control") {
				setTitle(Settings.NAME);
				setSize(Settings.CONTROL_SIZE);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setLayout(new BorderLayout());
				setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
			}
		}
	
		public ElementWindow(final Rectangle r) {
			super();
			setTitle("Display");
			setUndecorated(true);
			setIconImage(Settings.ICON.getImage());
			setSize(r.getSize());
			setLocation(r.getLocation());
			setBackground(new Color(0, 0, 0, 0));
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			final Border b = new BevelBorder(1, Color.gray, Color.gray);
			this.getRootPane().setBorder(b);
		}
		
		private static final long serialVersionUID = 1L;
	}
}
