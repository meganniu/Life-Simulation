import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.javafx.geom.Rectangle;

public class Main extends JFrame implements KeyListener, ActionListener {

	boolean startSim; // true to start sim, false to pause sim
	private static int drawWidth = 600, drawHeight = 600;
	GamePane gamePane = new GamePane(drawWidth, drawHeight);
	JButton up = new JButton("^");
	JButton right = new JButton(">");
	JButton down = new JButton("v");
	JButton left = new JButton("<");

	static StatsPanel statsPanel = new StatsPanel();

	public Main() {

		addKeyListener(this);
		setFocusable(true);
		this.requestFocusInWindow();

		GamePane.drawRegion = new Rectangle(0, 0, drawWidth*2, drawHeight*2);
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JButton go = new JButton("Go");
		go.addActionListener(this);
		startSim = false;

		up.addActionListener(this);
		down.addActionListener(this);
		right.addActionListener(this);
		left.addActionListener(this);

		System.out.println("here2");

		gbc.insets = new Insets(2,5,2,5);
		gbc.gridheight = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(statsPanel, gbc);

		gbc.gridx = 1;
		add(gamePane, gbc);

		gbc.gridheight = 1;
		gbc.gridx = 2;
		add(go, gbc);

		JPanel controlPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.BOTH;
		c2.gridx = 1;
		controlPanel.add(up, c2);
		c2.gridy = 1;
		c2.gridx = 0;
		controlPanel.add(left, c2);
		c2.gridx = 2;
		controlPanel.add(right, c2);
		c2.gridy = 2;
		c2.gridx = 1;
		controlPanel.add(down, c2);

		gbc.gridy = 1;
		add(controlPanel, gbc);

		setSize(1000, 800);
		pack();
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gamePane.render();
		gamePane.render();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Go")) {
			if (startSim == false) {
				gamePane.start();
			} else {
				gamePane.stop();
			}
			startSim = !startSim;
		}
		if (e.getSource() == up) {
			if (GamePane.drawRegion.y - 100 >= 0)
				GamePane.drawRegion.y -= 100;
		}
		if (e.getSource() == right) {
			if (GamePane.drawRegion.x + 100 + drawWidth*2 <= DrawArea.width)
				GamePane.drawRegion.x += 100;
		}
		if (e.getSource() == down) {
			if (GamePane.drawRegion.y + 100 + drawHeight*2 <= DrawArea.height)
				GamePane.drawRegion.y += 100;
		}
		if (e.getSource() == left) {
			if (GamePane.drawRegion.x - 100 >= 0)
				GamePane.drawRegion.x -= 100;
		}
		if (!GamePane.running)
			gamePane.render();
		requestFocus();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (GamePane.drawRegion.y - 100 >= 0)
				GamePane.drawRegion.y -= 100;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (GamePane.drawRegion.x + 100 + drawWidth*2 <= DrawArea.width)
				GamePane.drawRegion.x += 100;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (GamePane.drawRegion.y + 100 + drawHeight*2 <= DrawArea.height)
				GamePane.drawRegion.y += 100;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (GamePane.drawRegion.x - 100 >= 0)
				GamePane.drawRegion.x -= 100;
		}
		if (!GamePane.running)
			gamePane.render();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static void run() {

	}

	public static void main(String[] args) {
		Main Simulation = new Main();
	}

}