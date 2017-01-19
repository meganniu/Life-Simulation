import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

public class Main extends JFrame implements KeyListener, ActionListener {

	boolean startSim; // true to start sim, false to pause sim
	static int xShift = 0, yShift = 0;
	static int drawWidth = 800, drawHeight = 800;
	DrawPane drawPane = new DrawPane(drawWidth, drawHeight);
	JButton up = new JButton("^");
	JButton right = new JButton(">");
	JButton down = new JButton("v");
	JButton left = new JButton("<");

	public Main() {

		addKeyListener(this);
		setFocusable(true);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JButton go = new JButton("Go");
		go.addActionListener(this);
		startSim = false;

		System.out.println("here2");

		gbc.gridx = 0;
		gbc.gridy = 0;
		add(go, gbc);

		up.addActionListener(this);
		down.addActionListener(this);
		right.addActionListener(this);
		left.addActionListener(this);

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

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		// gbc.weighty = 0.8;

		add(drawPane, gbc);

		setSize(1000, 800);
		pack();
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Go")) {
			if (startSim == false) {
				DrawPane.timer.start();
			} else {
				DrawPane.timer.stop();
			}
			startSim = !startSim;
		}
		if (e.getSource() == up) {
			if (yShift - 50 >= 0)
				yShift -= 50;
		}
		if (e.getSource() == right) {
			if (xShift + 50 + drawWidth < DrawArea.width)
				xShift += 50;
		}
		if (e.getSource() == down) {
			if (yShift + 50 + drawHeight < DrawArea.height)
				yShift += 50;
		}
		if (e.getSource() == left) {
			if (xShift - 50 >= 0)
				xShift -= 50;
		}
		drawPane.repaint();
		requestFocus();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (yShift - 50 >= 0)
				yShift -= 50;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (xShift + 50 + drawWidth < DrawArea.width)
				xShift += 50;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (yShift + 50 + drawHeight < DrawArea.height)
				yShift += 50;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (xShift - 50 >= 0)
				xShift -= 50;
		}
		drawPane.repaint();
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
