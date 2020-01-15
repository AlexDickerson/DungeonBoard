package DungeonBoard.control;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import DungeonBoard.draw.DrawPanel;
import DungeonBoard.control.FileLoader;

public class ControlPaint extends JPanel {
	
	public ControlPaint() {
		setLayout(new BorderLayout());
		setFocusable(true);
		
		final JPanel northPanel = getNorthPanel();
		northPanel.setPreferredSize(new Dimension(400, 32));
		drawPanel = new DrawPanel();
		ControlPanelButtons.configureButtons(drawPanel, northPanel, this);
	}

	private JPanel getNorthPanel() {
		final JPanel northPanel = new JPanel();
		northPanel.setPreferredSize(new Dimension(400, 32));
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
		northPanel.repaint();
		northPanel.repaint();
		return northPanel;
	}

	void setFile(final String type) {
		final FileLoader fileLoader = new FileLoader(drawPanel, this);
		fileLoader.browse(type);
	}

	public void repaintDrawPanel(){
		drawPanel.repaint();
	}

	public void swapPanels(JPanel dungeonMasterDisplayPanel){
		if (Arrays.asList(getComponents()).contains(drawPanel)) {
			remove(drawPanel);
			add(dungeonMasterDisplayPanel);
		} else {
			add(drawPanel);
			remove(dungeonMasterDisplayPanel);
		}
		revalidate();
		repaint();
	}

	public BufferedImage createMask(){
		return drawPanel.createMask();
	}

	private static final long serialVersionUID = 1L;
	private DrawPanel drawPanel;
}