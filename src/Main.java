import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
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

	int startingCarnivores = 5;
	int startingHerbivores = 50;
	
	public class StartScreen extends JPanel implements MouseMotionListener, MouseListener {

		int shiftx = 500, shifty = 300;

		BufferedImage front = null, frontSel = null, btn1 = null, btn1Sel = null, btn2 = null, btn2Sel = null;
		//BufferedImage back[] = new BufferedImage[249];
		BufferedImage back = null;

		int frameCounter = 0;

		private Timer t = new Timer(40, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (frameCounter == 248)
					frameCounter = 0;
				else
					frameCounter++;
				repaint();
			}
		});

		boolean selected1, selected2, selected3;

		AudioClip click = new AudioClip(new File("sounds/click.wav").toURI().toString());

		Rectangle r1 = new Rectangle((getWidth() - 700) / 2 - (shiftx - 500) / 20 + 175,
				25 + (getHeight() - 400) / 2 - (shifty - 400) / 16, 350, 350);
		Rectangle r2 = new Rectangle(getWidth() / 2 - 250 - 50 - (shiftx - 500) / 13 + 63,
				10 + 400 - (shifty - 400) / 10, 125, 125);
		Rectangle r3 = new Rectangle(getWidth() / 2 + 50 - (shiftx - 500) / 13 + 63, 10 + 400 - (shifty - 400) / 10,
				125, 125);

		public StartScreen() {

			addMouseMotionListener(this);
			addMouseListener(this);
			
			try {
				/*back[0] = ImageIO.read(new File("images/backgroundw.jpg"));
				for (int i = 1; i <= 249; i++) {
					back[i - 1] = ImageIO.read(new File("images/gif/gif_Layer_" + i + ".png"));
					System.out.println("Loaded gif image #" + i);
					progress.setValue(progress.getValue() + 1);
				}*/
				back = ImageIO.read(new File("images/backgroundw.png"));
				System.out.println("Loaded background");
				front = ImageIO.read(new File("images/foreground.png"));
				System.out.println("Loaded logo");
				frontSel = ImageIO.read(new File("images/foregroundS.png"));
				System.out.println("Loaded selected logo");
				btn1 = ImageIO.read(new File("images/btn1.png"));
				System.out.println("Loaded button");
				btn1Sel = ImageIO.read(new File("images/btn1S.png"));
				System.out.println("Loaded selected button");
				btn2 = ImageIO.read(new File("images/btn2.png"));
				System.out.println("Loaded button");
				btn2Sel = ImageIO.read(new File("images/btn2S.png"));
				System.out.println("Loaded selected button");
			} catch (IOException e) {
				e.printStackTrace();
			}

			setPreferredSize(new Dimension(1000, 600));
			setLayout(new GridBagLayout());

			t.start();

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
			g.drawImage(back.getSubimage(shiftx / 10, shifty / 6, getWidth()+50, getHeight()), 0, 0, null);

			if (selected1)
				g.drawImage(frontSel, (getWidth() - 730) / 2 - (shiftx - 500) / 20,
						(getHeight() - 420) / 2 - (shifty - 400) / 16 - 75, null);
			else
				g.drawImage(front, (getWidth() - 700) / 2 - (shiftx - 500) / 20,
						(getHeight() - 400) / 2 - (shifty - 400) / 16 - 75, null);

			if (selected2)
				g.drawImage(btn1Sel, getWidth() / 2 - 250 - 50 - (shiftx - 500) / 13 - 6, 400 - (shifty - 400) / 10 - 4,
						null);
			else
				g.drawImage(btn1, getWidth() / 2 - 250 - 50 - (shiftx - 500) / 13, 400 - (shifty - 400) / 10, null);

			if (selected3)
				g.drawImage(btn2Sel, getWidth() / 2 + 50 - (shiftx - 500) / 13 - 6, 400 - (shifty - 400) / 10 - 4,
						null);
			else
				g.drawImage(btn2, getWidth() / 2 + 50 - (shiftx - 500) / 13, 400 - (shifty - 400) / 10, null);
		}

		@Override
		public void mouseDragged(MouseEvent e) {

			if (r1.contains(e.getPoint())) {
				if (!selected1)
					click.play();
				selected1 = true;
			} else
				selected1 = false;

			shiftx = e.getX();
			shifty = e.getY();
			r1.x = 175 + (getWidth() - 700) / 2 - (shiftx - 500) / 20;
			r1.y = 25 + (getHeight() - 400) / 2 - (shifty - 400) / 16 - 75;
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {

			if (r1.contains(e.getPoint())) {
				if (!selected1)
					click.play();
				selected1 = true;
			} else
				selected1 = false;
			if (r2.contains(e.getPoint())) {
				if (!selected2)
					click.play();
				selected2 = true;
			} else
				selected2 = false;
			if (r3.contains(e.getPoint())) {
				if (!selected3)
					click.play();
				selected3 = true;
			} else
				selected3 = false;

			shiftx = e.getX();
			shifty = e.getY();

			r1.x = 175 + (getWidth() - 700) / 2 - (shiftx - 500) / 20;
			r1.y = 25 + (getHeight() - 400) / 2 - (shifty - 400) / 16 - 75;

			r2.x = getWidth() / 2 - 250 - 50 - (shiftx - 500) / 13 + 63;
			r2.y = 10 + 400 - (shifty - 400) / 10;

			r3.x = getWidth() / 2 + 50 - (shiftx - 500) / 13 + 63;
			r3.y = 10 + 400 - (shifty - 400) / 10;
			repaint();

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (r1.contains(e.getPoint()))
				generateGame();
			else if(r2.contains(e.getPoint()))
				getPreferences();

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
			if (r1.contains(e.getPoint()))
				generateGame();

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (r1.contains(e.getPoint()))
				generateGame();
		}
	}

	public class InstructionScreen extends JPanel implements MouseMotionListener, MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
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
			
			/**
			try{
				gamePane = new GamePane(drawWidth, drawHeight, Integer.parseInt(carnivoresTF.getText()), Integer.parseInt(herbivoresTF.getText()));
			}
			catch(NumberFormatException e1){
				try{
					gamePane = new GamePane(drawWidth, drawHeight, (int)(Math.random()*20) + 1, (int)(Math.random()*200) + 1);
				}
				catch(NumberFormatException e2){
					try{
						gamePane = new GamePane(drawWidth, drawHeight, Integer.parseInt(carnivoresTF.getText()), (int)(Math.random()*200) + 1);
					}
					catch(NumberFormatException e3){
						gamePane = new GamePane(drawWidth, drawHeight, (int)(Math.random()*20) + 1, Integer.parseInt(herbivoresTF.getText()));
					}
				}
			}
			*/
			
			GridBagConstraints gbc = new GridBagConstraints();

			startSim = false;

			up.addActionListener(this);
			down.addActionListener(this);
			right.addActionListener(this);
			left.addActionListener(this);
			go.addActionListener(this);

			gbc.insets = new Insets(10, 5, 10, 5);
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
			pack();
			gameStatus = true;

			generateGame();

		}
		requestFocus();
	}
	
	public class GetPreferences extends JFrame{
		JFrame frame = new JFrame();
		JButton set = new JButton("Set");
		JButton cancel = new JButton("Cancel");
		
		JLabel carnivoresLbl = new JLabel("Carnivores:");
		JLabel herbivoresLbl = new JLabel("Herbivores:");
		JTextField carnivoresTF = new JTextField();
		JTextField herbivoresTF = new JTextField();
		
		
		public GetPreferences(){
			frame.setSize(450,300);
			frame.setVisible(true);
			//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new GridBagLayout());
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			gbc.anchor = GridBagConstraints.PAGE_END;
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 0.4;
			gbc.insets = new Insets(0, 0, 10, 10);
			frame.add(carnivoresLbl, gbc);
			
			gbc.gridx = 1;
			gbc.insets = new Insets(0, 10, 10, 0);
			frame.add(herbivoresLbl, gbc);
			
			gbc.anchor = GridBagConstraints.PAGE_START;
			GhostText ghostTextC = new GhostText(carnivoresTF, "Carnivores to start");
			GhostText ghostTextH = new GhostText(herbivoresTF, "Herbivores to start");
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 0.3;
			gbc.insets = new Insets(10, 0, 0, 10);
			carnivoresTF.setPreferredSize(new Dimension(200, 20));
			frame.add(carnivoresTF, gbc);
			
			gbc.gridx = 1;
			gbc.insets = new Insets(10, 10, 0, 0);
			herbivoresTF.setPreferredSize(new Dimension(200, 20));
			frame.add(herbivoresTF, gbc);
			
			cancel.addActionListener(new MouseListener());
			set.addActionListener(new MouseListener());
			
			cancel.setPreferredSize(new Dimension(80, 30));
			set.setPreferredSize(new Dimension(80, 30));
			
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.weighty = 0.3;
			frame.add(cancel, gbc);
			
			gbc.gridx = 1;
			frame.add(set, gbc);
		}
		
		class MouseListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				if(e.getSource() == cancel){
					frame.setVisible(false);
					frame.dispose();
				}
				else if(e.getSource() == set){
					try{
						startingCarnivores = Integer.parseInt(carnivoresTF.getText());
					}
					catch(NumberFormatException ev){
					}
					
					try{
						startingHerbivores = Integer.parseInt(herbivoresTF.getText());
					}
					catch(NumberFormatException ev){
					}
					
					frame.setVisible(false);
					frame.dispose();
				}
			}
		}
	}

	public void getPreferences(){
		GetPreferences getPreferences = new GetPreferences();
	}
	
	public void generateGame() {
		System.out.println("startingCarnivores: " + startingCarnivores);
		System.out.println("startingHerbivores: " + startingHerbivores);
		gamePane = new GamePane(drawWidth, drawHeight, startingCarnivores, startingHerbivores);
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

	}
	
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