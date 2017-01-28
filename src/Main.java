import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javafx.scene.media.AudioClip;

public class Main extends JFrame implements KeyListener, ActionListener {

	JPanel gameScreen = new JPanel(new GridBagLayout());
	StartScreen startScreen = new StartScreen();
	boolean gameStatus = false;

	boolean startSim; // true to start sim, false to pause sim
	private static int drawWidth = 600, drawHeight = 600;
	GamePane gamePane;
	JButton up = new JButton("^");
	JButton right = new JButton(">");
	JButton down = new JButton("v");
	JButton left = new JButton("<");
	JButton go = new JButton("Go");
	static JButton startBtn = new JButton("Start");

	public class StartScreen extends JPanel implements MouseMotionListener, MouseListener {

		int shiftx = 500, shifty = 300;

		BufferedImage back = null, front = null, frontSel;

		boolean selected = false;

		AudioClip click = new AudioClip(new File("sounds/click.wav").toURI().toString());

		Rectangle r = new Rectangle((getWidth() - 700) / 2 - (shiftx - 500) / 20 + 175, (getHeight() - 400) / 2 - (shifty - 400) / 16, 350,
				350);

		public StartScreen() {

			addMouseMotionListener(this);
			addMouseListener(this);

			try {
				back = ImageIO.read(new File("images/backgroundw.jpg"));
				front = ImageIO.read(new File("images/foreground.png"));
				frontSel = ImageIO.read(new File("images/foregroundS.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			setPreferredSize(new Dimension(1000, 600));
			setLayout(new GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.insets = new Insets(300, 5, 5, 5);

			/*
			 * add(title, c);
			 * 
			 * c.insets = new Insets(5, 5, 5, 5); c.gridy = 1; add(startBtn, c);
			 * 
			 * c.gridy = 2; add(instBtn, c);
			 * 
			 * c.fill = GridBagConstraints.NONE; c.anchor =
			 * GridBagConstraints.SOUTHEAST; c.gridy = 3; c.weighty = 1;
			 * add(quitBtn, c);
			 */
		}

		public void paintComponent(Graphics g) {
			g.drawImage(back.getSubimage(shiftx / 10, shifty / 6, getWidth(), getHeight()), 0, 0, null);

			if (selected)
				g.drawImage(frontSel, (getWidth() - 770) / 2 - (shiftx - 500) / 20, (getHeight() - 440) / 2 - (shifty - 400) / 16, null);
			else
				g.drawImage(front, (getWidth() - 700) / 2 - (shiftx - 500) / 20, (getHeight() - 400) / 2 - (shifty - 400) / 16, null);
			g.drawRect(r.x, r.y, r.width, r.height);
		}

		@Override
		public void mouseDragged(MouseEvent e) {

			if (r.contains(e.getPoint()))
				selected = true;
			else
				selected = false;

			shiftx = e.getX();
			shifty = e.getY();
			r.x = 175 + (getWidth() - 700) / 2 - (shiftx - 500) / 20;
			r.y = 25 + (getHeight() - 400) / 2 - (shifty - 400) / 16;
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {

			if (r.contains(e.getPoint())) {
				if (!selected)
					click.play();
				selected = true;
			} else
				selected = false;

			shiftx = e.getX();
			shifty = e.getY();
			r.x = 175 + (getWidth() - 700) / 2 - (shiftx - 500) / 20;
			r.y = 25 + (getHeight() - 400) / 2 - (shifty - 400) / 16;
			repaint();

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (r.contains(e.getPoint()))
				generateGame();

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (r.contains(e.getPoint()))
				generateGame();

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (r.contains(e.getPoint()))
				generateGame();
		}
	}

	/*
	 * public class StartScreen extends JPanel { JTextField carnivoresTF = new
	 * JTextField(); JTextField herbivoresTF = new JTextField();
	 * 
	 * JLabel carnivoresLbl = new JLabel("Carnivores:"); JLabel herbivoresLbl =
	 * new JLabel("Herbivores:");
	 * 
	 * public StartScreen() { this.setPreferredSize(new Dimension(1000, 800));
	 * this.setLayout(new GridBagLayout());
	 * 
	 * GridBagConstraints gbc = new GridBagConstraints();
	 * 
	 * gbc.anchor = GridBagConstraints.PAGE_END;
	 * 
	 * gbc.gridx = 0; gbc.gridy = 0; gbc.weighty = 0.7; gbc.insets = new
	 * Insets(0, 0, 10, 10); this.add(carnivoresLbl, gbc);
	 * 
	 * gbc.gridx = 1; gbc.insets = new Insets(0, 10, 10, 0);
	 * this.add(herbivoresLbl, gbc);
	 * 
	 * gbc.anchor = GridBagConstraints.PAGE_START; GhostText ghostTextC = new
	 * GhostText(carnivoresTF, "Carnivores to start"); GhostText ghostTextH =
	 * new GhostText(herbivoresTF, "Herbivores to start");
	 * 
	 * gbc.gridx = 0; gbc.gridy = 1; gbc.weighty = 0.05; gbc.insets = new
	 * Insets(0, 0, 0, 10); carnivoresTF.setPreferredSize(new Dimension(200,
	 * 20)); this.add(carnivoresTF, gbc);
	 * 
	 * gbc.gridx = 1; gbc.insets = new Insets(0, 10, 0, 0);
	 * herbivoresTF.setPreferredSize(new Dimension(200, 20));
	 * this.add(herbivoresTF, gbc);
	 * 
	 * gbc.insets = new Insets(0, 0, 0, 0); gbc.gridx = 0; gbc.gridy = 2;
	 * gbc.gridwidth = 2; gbc.weighty = 0.1; gbc.anchor =
	 * GridBagConstraints.PAGE_START; gbc.fill = GridBagConstraints.HORIZONTAL;
	 * this.add(Main.startBtn, gbc);
	 * 
	 * }
	 * 
	 * }
	 */

	static StatsPanel statsPanel = new StatsPanel();

	public Main() {

		addKeyListener(this);
		setFocusable(true);
		this.requestFocusInWindow();

		GamePane.drawRegion = new Rectangle(0, 0, drawWidth * 2, drawHeight * 2);

		startBtn.addActionListener(this);

		setContentPane(startScreen);

		setSize(1000, 600);
		setExtendedState(JFrame.NORMAL);
		pack();
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (gameStatus) {
			if (e.getSource() == go) {
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
				if (GamePane.drawRegion.x + 100 + drawWidth * 2 <= DrawArea.width)
					GamePane.drawRegion.x += 100;
			}
			if (e.getSource() == down) {
				if (GamePane.drawRegion.y + 100 + drawHeight * 2 <= DrawArea.height)
					GamePane.drawRegion.y += 100;
			}
			if (e.getSource() == left) {
				if (GamePane.drawRegion.x - 100 >= 0)
					GamePane.drawRegion.x -= 100;
			}
			if (!GamePane.running && gameStatus)
				gamePane.render();
		}
		if (e.getSource() == startBtn) {
			generateGame();
		}
		requestFocus();
	}

	public void generateGame() {
		gamePane = new GamePane(drawWidth, drawHeight);
		GridBagConstraints gbc = new GridBagConstraints();

		startSim = false;

		up.addActionListener(this);
		down.addActionListener(this);
		right.addActionListener(this);
		left.addActionListener(this);
		go.addActionListener(this);

		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.gridheight = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gameScreen.add(statsPanel, gbc);

		gbc.gridx = 1;
		gameScreen.add(gamePane, gbc);

		gbc.gridheight = 1;
		gbc.gridx = 2;
		gameScreen.add(go, gbc);

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
		gameScreen.add(controlPanel, gbc);

		setContentPane(gameScreen);
		revalidate();
		gameStatus = true;
		System.out.println(getWidth() + " " + getHeight());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameStatus) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (GamePane.drawRegion.y - 100 >= 0)
					GamePane.drawRegion.y -= 100;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (GamePane.drawRegion.x + 100 + drawWidth * 2 <= DrawArea.width)
					GamePane.drawRegion.x += 100;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (GamePane.drawRegion.y + 100 + drawHeight * 2 <= DrawArea.height)
					GamePane.drawRegion.y += 100;
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (GamePane.drawRegion.x - 100 >= 0)
					GamePane.drawRegion.x -= 100;
			}
			if (!GamePane.running && gameStatus)
				gamePane.render();
		}
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
		Main simulation = new Main();

		class GhostText implements FocusListener, DocumentListener, PropertyChangeListener {
			private final JTextField textfield;
			private boolean isEmpty;
			private Color ghostColor;
			private Color foregroundColor;
			private final String ghostText;

			protected GhostText(final JTextField textfield, String ghostText) {
				super();
				this.textfield = textfield;
				this.ghostText = ghostText;
				this.ghostColor = Color.LIGHT_GRAY;
				textfield.addFocusListener(this);
				registerListeners();
				updateState();
				if (!this.textfield.hasFocus()) {
					focusLost(null);
				}
			}

			public void delete() {
				unregisterListeners();
				textfield.removeFocusListener(this);
			}

			private void registerListeners() {
				textfield.getDocument().addDocumentListener(this);
				textfield.addPropertyChangeListener("foreground", this);
			}

			private void unregisterListeners() {
				textfield.getDocument().removeDocumentListener(this);
				textfield.removePropertyChangeListener("foreground", this);
			}

			public Color getGhostColor() {
				return ghostColor;
			}

			public void setGhostColor(Color ghostColor) {
				this.ghostColor = ghostColor;
			}

			private void updateState() {
				isEmpty = textfield.getText().length() == 0;
				foregroundColor = textfield.getForeground();
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (isEmpty) {
					unregisterListeners();
					try {
						textfield.setText("");
						textfield.setForeground(foregroundColor);
					} finally {
						registerListeners();
					}
				}

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (isEmpty) {
					unregisterListeners();
					try {
						textfield.setText(ghostText);
						textfield.setForeground(ghostColor);
					} finally {
						registerListeners();
					}
				}
			}

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateState();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateState();
			}

			public void insertUpdate(DocumentEvent e) {
				updateState();
			}

			public void removeUpdate(DocumentEvent e) {
				updateState();
			}

		}
	}
}