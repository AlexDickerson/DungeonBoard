package DungeonBoard.main;

import DungeonBoard.InitiativeTracker.InitiativePanel;
import DungeonBoard.control.ControlPaint;
import DungeonBoard.display.DisplayPaint;
import DungeonBoard.display.DisplayVideo;
import java.awt.Point;

public class DisplayVideoCombo {
    public DisplayVideoCombo(){
		controlPanel = new ControlPaint();
		initiativePanel = new InitiativePanel();
        playerDisplayPanel = new DisplayPaint("player");
        dungeonMasterDisplayPanel = new DisplayPaint("dm");
		videoPlayerPanel = new DisplayVideo();
	}

    public void repaintDisplays(){
		controlPanel.repaintDrawPanel();
		dungeonMasterDisplayPanel.repaint();
		playerDisplayPanel.repaint();
	}

	public void resetImages(){
		controlPanel.repaintDrawPanel();
		dungeonMasterDisplayPanel.resetImage();
		playerDisplayPanel.resetImage();
	}

	public void setMask(){
		playerDisplayPanel.setMask(controlPanel.createMask());
		playerDisplayPanel.setImageSize();
		dungeonMasterDisplayPanel.setMask(controlPanel.createMask());
		dungeonMasterDisplayPanel.setImageSize();
	}

	public void setImageSize(){
		playerDisplayPanel.setImageSize();
		dungeonMasterDisplayPanel.setImageSize();
	}

	public void setWindows(double zoom, Point position){
		setImageSize();
		playerDisplayPanel.setWindow(zoom, position);
		dungeonMasterDisplayPanel.setWindow(zoom, position);
	}

	public void setWindowPositions(Point position){
		setImageSize();
		playerDisplayPanel.setWindowPos(position);
		dungeonMasterDisplayPanel.setWindowPos(position);
	}

	public void swapControlGMDisplay(){ controlPanel.swapPanels(dungeonMasterDisplayPanel); }

	public void addCharacter(String name){ initiativePanel.addCharacter(name); }

	public void playVideo(String path){
		repaintDisplays();
		videoPlayerPanel.playVideo(path);
	}
	
    private DisplayPaint dungeonMasterDisplayPanel;
    public DisplayPaint playerDisplayPanel;
	public DisplayVideo videoPlayerPanel;
	public ControlPaint controlPanel;
	public InitiativePanel initiativePanel;
}