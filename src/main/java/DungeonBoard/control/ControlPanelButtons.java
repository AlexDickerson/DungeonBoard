package DungeonBoard.control;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import DungeonBoard.main.Main;
import DungeonBoard.main.Settings;
import DungeonBoard.draw.DrawPanel;
import com.formdev.flatlaf.icons.*;

class ControlPanelButtons {

    ControlPanelButtons(){

	}

    public static void configureButtons(final DrawPanel draw, final JPanel innerNorthPanel, final ControlPaint control) {
		drawPanel = draw;
		final JButton drawStyleButton = Settings.createButton(Settings.DRAW_STYLE[0]);
		drawStyleButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				drawPanel.toggleStyle();
				drawStyleButton.setIcon(Settings.DRAW_STYLE[drawPanel.getStyle()]);
			}
		});

		final JButton shape = Settings.createButton(Settings.PEN_TYPE[0]);
		shape.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				drawPanel.togglePen();
				shape.setIcon(Settings.PEN_TYPE[drawPanel.getPen()]);
			}
		});

		final JButton drawModeButton = Settings.createButton(Settings.DRAW_MODE[0]);
		drawModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				drawPanel.toggleDrawMode();
				drawModeButton.setIcon(Settings.DRAW_MODE[drawPanel.getDrawMode()]);
			}
		});

		final JButton browseButton = Settings.createButton(new FlatFileViewDirectoryIcon());
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				control.setFile("map");
			}
		});

		final JButton switchPanel = Settings.createButton("GM Display");
		switchPanel.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Main.swapControlGMDisplay();
			}
		});

		final JButton showTokens = Settings.createButton("Show Tokens");
		showTokens.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if(Settings.displayTokens == 0){ 
					Settings.displayTokens = 1;
					showTokens.setText("Hide Tokens");
				}
				else{
					Settings.displayTokens = 0;
					showTokens.setText("Show Tokens");
				} 
				Main.repaintDisplays();
			}
		});

		final JButton updateButton = Settings.createButton("Update Screen");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				updateScreen();
			}
		});

		final JButton showButton = Settings.createButton("Show");
		showButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				drawPanel.showAll();
				updateScreen();
			}
		});

		final JButton hideButton = Settings.createButton("Hide");
		hideButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				drawPanel.hideAll();
				updateScreen();
			}
		});

		final JButton tokenButton = Settings.createButton("Tokens");
		tokenButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				control.setFile("token");
			}
		});

		final JButton gridButton = Settings.createButton("Enable Grid");
		gridButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				flipGrid();
				if (Settings.displayGrid == 1)
					gridButton.setText("Disable Grid");
				else
					gridButton.setText("Enable Grid");
				Main.repaintDisplays();
			}
		});

		final SpinnerModel model = new SpinnerNumberModel(32, 1, 9999, 1);
		final JSpinner spinner = new JSpinner(model);
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				Settings.screenSize = (Integer) spinner.getValue();
				if (Settings.displayGrid == 1)
					updateButton.setEnabled(true);
			}
		});

		final JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 10, 100, 25);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent arg0) {
				drawPanel.setRadius(slider.getValue());
			}
		});

		innerNorthPanel.add(drawStyleButton);
		innerNorthPanel.add(shape);
		innerNorthPanel.add(drawModeButton);
		innerNorthPanel.add(browseButton);
		innerNorthPanel.add(switchPanel);
		innerNorthPanel.add(showButton);
		innerNorthPanel.add(hideButton);
		innerNorthPanel.add(tokenButton);
		innerNorthPanel.add(showTokens);
		innerNorthPanel.add(gridButton);
		innerNorthPanel.add(spinner);

		slider.setMinimumSize(new Dimension(200, 16));
		slider.setVisible(false);
		innerNorthPanel.add(slider);

		final JLabel fileNameLabel = new JLabel(" ");
		fileNameLabel.setPreferredSize(new Dimension(4000, 16));
		innerNorthPanel.add(fileNameLabel);

		control.add(innerNorthPanel, BorderLayout.NORTH);
		control.add(drawPanel, BorderLayout.CENTER);

		control.setVisible(true);
	}

	private static void updateScreen(){
		if (drawPanel.hasImage()) {
			try {
				Main.setMask();
			} catch (final OutOfMemoryError error) {
				Main.showError("Cannot update Image, file is probably large", error);
			}
		}
	}

	public static void flipGrid(){
		if(Settings.displayGrid == 0) Settings.displayGrid = 1;
		else Settings.displayGrid = 0;
		drawPanel.repaint();
	}

	

	private static DrawPanel drawPanel;
}