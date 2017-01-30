import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
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
	InstructionScreen instructionScreen = new InstructionScreen();

	boolean gameStatus = false;
	boolean settingsOpen = false;

	boolean startSim; // true to start sim, false to pause sim
	private static int drawWidth = 600, drawHeight = 600;
	GamePane gamePane;
	JButton up = new JButton("^");
	JButton right = new JButton(">");
	JButton down = new JButton("v");
	JButton left = new JButton("<");
	JButton go = new JButton("Go");
	static JButton startBtn = new JButton("Start");

	int startingCarnivores = 3; 
	int startingHerbivores = 20; 
	int startMinSpeed = 2, startMaxSpeed = 9; 
	int startMinRad = 80, startMaxRad = 100;
	int startMinEgg = 20000, startMaxEgg = 40000;
	double startMinEnergy = 6000.0, startMaxEnergy = 9000.0;
	double startMinMetabolism = 80.0, startMaxMetabolism = 120.0;
	double startMinFood = 200.0, startMaxFood = 800.0;
	
	public class StartScreen extends JPanel implements MouseMotionListener, MouseListener {

		int shiftx = 500, shifty = 300;

		// Parallax factors (B = back, F = front, Btn = buttons --- X is x
		// factor, Y is y factor)

		// Background moves faster than foreground
		// double PBX = 10, PBY = 6, PFX = 20, PFY = 16, PBtnX = 13, PBtnY = 10;

		// Background moves slower than foreground
		double PBX = 20, PBY = 16, PFX = 10, PFY = 6, PBtnX = 13, PBtnY = 10;

		BufferedImage front = null, frontSel = null, btn1 = null, btn1Sel = null, btn2 = null, btn2Sel = null;
		BufferedImage back = null;

		boolean selected1, selected2, selected3;

		AudioClip click = new AudioClip(new File("sounds/click.wav").toURI().toString());

		Shape r1 = new Ellipse2D.Double((getWidth() - 730) / 2 - (int) ((shiftx - 500) / PFX) + 175 + 12,
				(getHeight() - 420) / 2 - (int) ((shifty - 400) / PFY) - 75 + 32, 355, 355);
		Shape r2 = new Ellipse2D.Double(getWidth() / 2 - 250 - 50 - (int) ((shiftx - 500) / PBtnX) + 63,
				10 + 400 - (int) ((shifty - 400) / PBtnY), 125, 125);
		Shape r3 = new Ellipse2D.Double(getWidth() / 2 + 50 - (int) ((shiftx - 500) / PBtnX) + 63,
				10 + 400 - (int) ((shifty - 400) / PBtnY), 125, 125);

		public StartScreen() {

			addMouseMotionListener(this);
			addMouseListener(this);

			try {
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
		}

		public void paintComponent(Graphics g) {
			g.drawImage(back.getSubimage((int) (shiftx / PBX), (int) (shifty / PBY), getWidth() + 50, getHeight()), 0,
					0, null);

			if (selected1)
				g.drawImage(frontSel, (getWidth() - 730) / 2 - (int) ((shiftx - 500) / PFX),
						(getHeight() - 420) / 2 - (int) ((shifty - 400) / PFY) - 75, null);
			else
				g.drawImage(front, (getWidth() - 700) / 2 - (int) ((shiftx - 500) / PFX),
						(getHeight() - 400) / 2 - (int) ((shifty - 400) / PFY) - 75, null);

			if (selected2)
				g.drawImage(btn1Sel, getWidth() / 2 - 250 - 50 - (int) ((shiftx - 500) / PBtnX) - 6,
						400 - (int) ((shifty - 400) / PBtnY) - 4, null);
			else
				g.drawImage(btn1, getWidth() / 2 - 250 - 50 - (int) ((shiftx - 500) / PBtnX),
						400 - (int) ((shifty - 400) / PBtnY), null);

			if (selected3)
				g.drawImage(btn2Sel, getWidth() / 2 + 50 - (int) ((shiftx - 500) / PBtnX) - 6,
						400 - (int) ((shifty - 400) / PBtnY) - 4, null);
			else
				g.drawImage(btn2, getWidth() / 2 + 50 - (int) ((shiftx - 500) / PBtnX),
						400 - (int) ((shifty - 400) / PBtnY), null);
		}

		@Override
		public void mouseDragged(MouseEvent e) {

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

			r1 = new Ellipse2D.Double((getWidth() - 730) / 2 - (int) ((shiftx - 500) / PFX) + 175 + 12,
					(getHeight() - 420) / 2 - (int) ((shifty - 400) / PFY) - 75 + 32, 355, 355);
			r2 = new Ellipse2D.Double(getWidth() / 2 - 250 - 50 - (int) ((shiftx - 500) / PBtnX) + 63,
					10 + 400 - (int) ((shifty - 400) / PBtnY), 125, 125);
			r3 = new Ellipse2D.Double(getWidth() / 2 + 50 - (int) ((shiftx - 500) / PBtnX) + 63,
					10 + 400 - (int) ((shifty - 400) / PBtnY), 125, 125);
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

			r1 = new Ellipse2D.Double((getWidth() - 730) / 2 - (int) ((shiftx - 500) / PFX) + 175 + 12,
					(getHeight() - 420) / 2 - (int) ((shifty - 400) / PFY) - 75 + 32, 355, 355);
			r2 = new Ellipse2D.Double(getWidth() / 2 - 250 - 50 - (int) ((shiftx - 500) / PBtnX) + 63,
					10 + 400 - (int) ((shifty - 400) / PBtnY), 125, 125);
			r3 = new Ellipse2D.Double(getWidth() / 2 + 50 - (int) ((shiftx - 500) / PBtnX) + 63,
					10 + 400 - (int) ((shifty - 400) / PBtnY), 125, 125);
			repaint();

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (r1.contains(e.getPoint()))
				generateGame();
			else if (r2.contains(e.getPoint()))
				instructions(shifty);
			else if (r3.contains(e.getPoint()))
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
		
		public void mousePressed(MouseEvent e){
			
		}
		/**
		@Override
		public void mousePressed(MouseEvent e) {
			if (r1.contains(e.getPoint()))
				generateGame();
			else if (r2.contains(e.getPoint()))
				instructions();
			else if (r3.contains(e.getPoint()))
				getPreferences();

		}
		**/

		@Override
		public void mouseReleased(MouseEvent e) {
			if (r1.contains(e.getPoint()))
				generateGame();
			else if (r2.contains(e.getPoint()))
				instructions(shifty);
			else if (r3.contains(e.getPoint()))
				getPreferences();
		}
	}

	public class InstructionScreen extends JPanel implements MouseMotionListener, MouseListener {

		int shifty = 420;

		BufferedImage front = null, back = null;

		public InstructionScreen() {

			addMouseMotionListener(this);
			addMouseListener(this);

			try {
				back = ImageIO.read(new File("images/backInstructions.png"));
				System.out.println("Loaded background");
				front = ImageIO.read(new File("images/instructions.png"));
				System.out.println("Loaded instructions");
			} catch (IOException e) {
				e.printStackTrace();
			}

			setPreferredSize(new Dimension(1000, 600));
			setLayout(new GridBagLayout());
		}

		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(back.getSubimage(0, shifty / 3, getWidth(), getHeight()), 0, 0, null);
			int y = shifty;
			if (y > 500)
				y = 500;
			if (y < 50)
				y = 50;
			BufferedImage bi = front.getSubimage(0, y - 50, 1000, 600);
			g.drawImage(bi, (getWidth() - front.getWidth()) / 2, (getHeight() - 600) / 2, null);
		}

		public void mouseClicked(MouseEvent e) {
			leaveInstructions();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			leaveInstructions();

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			leaveInstructions();

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			shifty = e.getY();
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			shifty = e.getY();
			repaint();

		}

		public void setShiftY(int shifty) {
			this.shifty = shifty;
			
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
		setTitle("Evolution Simulator - By Ayon B. Megan N. and Vincent W.");
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
		requestFocus();
	}

	public void instructions(int shifty) {
		instructionScreen.setShiftY(shifty);
		setContentPane(instructionScreen);
		revalidate();
	}

	public void leaveInstructions() {
		setContentPane(startScreen);
		revalidate();
	}

	public class GetPreferences extends JFrame implements WindowListener {
		JButton set = new JButton("Set");
		JButton cancel = new JButton("Cancel");
		
		JLabel carnivoresLbl = new JLabel("Carnivores");
		JLabel minSpeedLbl = new JLabel("Minimum Starting Speed");
		JLabel minRadLbl = new JLabel("Minimum Starting Detect Radius:");
		JLabel minEggLbl = new JLabel("Minimum Starting Egg Cycle Length:");
		JLabel minEnergyLbl = new JLabel("Minimum Starting Energy:");
		JLabel minMetabolismLbl = new JLabel("Minimum Starting Metabolism:");
		
		JLabel herbivoresLbl = new JLabel("Herbivores:");
		JLabel maxSpeedLbl = new JLabel("Maximum Starting Speed");
		JLabel maxRadLbl = new JLabel("Maximum Starting Detect Radius:");
		JLabel maxEggLbl = new JLabel("Maximum Starting Egg Cycle Length:");
		JLabel maxEnergyLbl = new JLabel("Maximum Starting Energy:");
		JLabel maxMetabolismLbl = new JLabel("Maximum Starting Metabolism:");
		
		JLabel minFoodLbl = new JLabel("Minimum Food nutrition");
		JLabel maxFoodLbl = new JLabel("Maximum Food nutrition");
		
		JTextField carnivoresTF = new JTextField();
		JTextField minSpeedTF = new JTextField();
		JTextField minRadTF = new JTextField();
		JTextField minEggTF = new JTextField();
		JTextField minEnergyTF = new JTextField();
		JTextField minMetabolismTF = new JTextField();
		JTextField minFoodTF = new JTextField();
		
		JTextField herbivoresTF = new JTextField();
		JTextField maxSpeedTF = new JTextField();
		JTextField maxRadTF = new JTextField();
		JTextField maxEggTF = new JTextField();
		JTextField maxEnergyTF = new JTextField();
		JTextField maxMetabolismTF = new JTextField();
		JTextField maxFoodTF = new JTextField();
		
		

		public GetPreferences() {

			System.out.println("Created settings");

			setTitle("Customization");
			setSize(450, 500);
			setVisible(true);
			setDefaultCloseOperation(JDISPOSE_ON_CLOSE);
			setLocationRelativeTo(null);
			setLayout(new GridBagLayout());

			addWindowListener(this);
			
			GridBagConstraints gbc = new GridBagConstraints();

			gbc.anchor = GridBagConstraints.PAGE_END;

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.insets = new Insets(0, 0, 0, 10);
			add(carnivoresLbl, gbc);
			
			gbc.gridy = 2;
			add(minSpeedLbl, gbc);
			
			gbc.gridy = 4;
			add(minRadLbl, gbc);
			
			gbc.gridy = 6;
			add(minEggLbl, gbc);
			
			gbc.gridy = 8;
			add(minEnergyLbl, gbc);
			
			gbc.gridy = 10;
			add(minMetabolismLbl, gbc);
			
			gbc.gridy = 12;
			add(minFoodLbl, gbc);
			
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.insets = new Insets(0, 10, 0, 0);
			add(herbivoresLbl, gbc);
			
			gbc.gridy = 2;
			add(maxSpeedLbl, gbc);
			
			gbc.gridy = 4;
			add(maxRadLbl, gbc);
			
			gbc.gridy = 6;
			add(maxEggLbl, gbc);
			
			gbc.gridy = 8;
			add(maxEnergyLbl, gbc);
			
			gbc.gridy = 10;
			add(maxMetabolismLbl, gbc);
			
			gbc.gridy = 12;
			add(maxFoodLbl, gbc);
			
			gbc.anchor = GridBagConstraints.PAGE_START;
			GhostText ghostTextC = new GhostText(carnivoresTF, "Carnivores to start");
			GhostText ghostTextMinSpeed = new GhostText(minSpeedTF, "Default 2");
			GhostText ghostTextMinRad = new GhostText(minRadTF, "Default 80");
			GhostText ghostTextMinEgg = new GhostText(minEggTF, "Default 20000");
			GhostText ghostTextMinEnergy = new GhostText(minEnergyTF, "Default 6000.0");
			GhostText ghostTextMinMetabolism = new GhostText(minMetabolismTF, "Default 80.0");
			GhostText ghostTextMinFood = new GhostText(minFoodTF, "Default 200.0");
			
			GhostText ghostTextH = new GhostText(herbivoresTF, "Herbivores to start");
			GhostText ghostTextMaxSpeed = new GhostText(maxSpeedTF, "Default 9");
			GhostText ghostTextMaxRad = new GhostText(maxRadTF, "Default 100");
			GhostText ghostTextMaxEgg = new GhostText(maxEggTF, "Default 40000");
			GhostText ghostTextMaxEnergy = new GhostText(maxEnergyTF, "Default 9000.0");
			GhostText ghostTextMaxMetabolism = new GhostText(maxMetabolismTF, "Default 120.0");
			GhostText ghostTextMaxCarn = new GhostText(maxFoodTF, "Default 800.0");
			
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 0.7;
			gbc.insets = new Insets(0, 0, 10, 10);
			carnivoresTF.setPreferredSize(new Dimension(200, 20));
			add(carnivoresTF, gbc);
			
			gbc.gridy = 3;
			minSpeedTF.setPreferredSize(new Dimension(200, 20));
			add(minSpeedTF, gbc);
			
			gbc.gridy = 5;
			minRadTF.setPreferredSize(new Dimension(200, 20));
			add(minRadTF, gbc);
			
			gbc.gridy = 7;
			minEggTF.setPreferredSize(new Dimension(200, 20));
			add(minEggTF, gbc);
			
			gbc.gridy = 9;
			minEnergyTF.setPreferredSize(new Dimension(200, 20));
			add(minEnergyTF, gbc);
			
			gbc.gridy = 11;
			minMetabolismTF.setPreferredSize(new Dimension(200, 20));
			add(minMetabolismTF, gbc);
			
			gbc.gridy = 13;
			minFoodTF.setPreferredSize(new Dimension(200, 20));
			add(minFoodTF, gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 10, 10, 0);
			herbivoresTF.setPreferredSize(new Dimension(200, 20));
			add(herbivoresTF, gbc);
			
			gbc.gridy = 3;
			maxSpeedTF.setPreferredSize(new Dimension(200, 20));
			add(maxSpeedTF, gbc);
			
			gbc.gridy = 5;
			maxRadTF.setPreferredSize(new Dimension(200, 20));
			add(maxRadTF, gbc);
			
			gbc.gridy = 7;
			maxEggTF.setPreferredSize(new Dimension(200, 20));
			add(maxEggTF, gbc);
			
			gbc.gridy = 9;
			maxEnergyTF.setPreferredSize(new Dimension(200, 20));
			add(maxEnergyTF, gbc);
			
			gbc.gridy = 11;
			maxMetabolismTF.setPreferredSize(new Dimension(200, 20));
			add(maxMetabolismTF, gbc);
			
			gbc.gridy = 13;
			maxFoodTF.setPreferredSize(new Dimension(200, 20));
			add(maxFoodTF, gbc);
			
			cancel.addActionListener(new MouseListener());
			set.addActionListener(new MouseListener());

			cancel.setPreferredSize(new Dimension(80, 30));
			set.setPreferredSize(new Dimension(80, 30));

			gbc.gridx = 0;
			gbc.gridy = 14;
			gbc.weighty = 0.3;
			add(cancel, gbc);

			gbc.gridx = 1;
			add(set, gbc);
		}

		class MouseListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == cancel) {
					setVisible(false);
					dispose();
				} else if (e.getSource() == set) {
					try {
						startingCarnivores = Integer.parseInt(carnivoresTF.getText());
					} catch (NumberFormatException ev) {
					}

					try {
						startingHerbivores = Integer.parseInt(herbivoresTF.getText());
					} 
					catch (NumberFormatException ev) {
					}

					try{
						startMinSpeed = Integer.parseInt(minSpeedTF.getText());
					}
					catch(NumberFormatException ev){
					}
					
					try{
						startMaxSpeed = Integer.parseInt(maxSpeedTF.getText());
					}
					catch(NumberFormatException ev){
					}
					try{
						startMinRad = Integer.parseInt(minRadTF.getText());
					}
					catch(NumberFormatException ev){
					}
					
					try{
						startMaxRad = Integer.parseInt(maxRadTF.getText());
					}
					catch(NumberFormatException ev){
					}
					try{
						startMinEgg = Integer.parseInt(minEggTF.getText());
					}
					catch(NumberFormatException ev){
					}
					
					try{
						startMaxEgg = Integer.parseInt(maxEggTF.getText());
					}
					catch(NumberFormatException ev){
					}
					try{
						startMinEnergy = Integer.parseInt(minEnergyTF.getText());
					}
					catch(NumberFormatException ev){
					}
					
					try{
						startMaxEnergy = Integer.parseInt(maxEnergyTF.getText());
					}
					catch(NumberFormatException ev){
					}
					try{
						startMinMetabolism = Integer.parseInt(minMetabolismTF.getText());
					}
					catch(NumberFormatException ev){
					}
					
					try{
						startMaxMetabolism = Integer.parseInt(maxMetabolismTF.getText());
					}
					catch(NumberFormatException ev){
					}
					try{
						startMinFood = Integer.parseInt(minFoodTF.getText());
					}
					catch(NumberFormatException ev){
					}
					try{
						startMaxFood = Integer.parseInt(maxFoodTF.getText());
					}
					catch(NumberFormatException ev){
					}
					frame.setVisible(false);
					frame.dispose();
				}
			}
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosed(WindowEvent e) {
			settingsOpen = false;
		}

		@Override
		public void windowClosing(WindowEvent e) {
			settingsOpen = false;

		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub

		}
	}


	public void getPreferences() {
		if (!settingsOpen) {
			GetPreferences getPreferences = new GetPreferences();
			settingsOpen = true;
		}
	}

	public void generateGame() {
		
		sort();
		gamePane = new GamePane(drawWidth, drawHeight, 
				startingCarnivores, startingHerbivores, 
				startMinSpeed, startMaxSpeed, 
				startMinRad, startMaxRad, 
				startMinEgg, startMaxEgg, 
				startMinEnergy, startMaxEnergy, 
				startMinMetabolism, startMaxMetabolism, 
				startMinFood, startMaxFood);
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

	public void sort(){
		int temp;
		double temp2;
		if (startingCarnivores < 0)
			startingCarnivores = 0;
		else if (startingCarnivores > 50)
			startingCarnivores = 50;
		
		if (startingHerbivores < 0)
			startingHerbivores = 0;
		else if (startingHerbivores > 50)
			startingHerbivores = 50;
		
		if (startMinSpeed > startMaxSpeed){
			temp = startMinSpeed;
			startMinSpeed = startMaxSpeed;
			startMaxSpeed = temp;
		}
		
		if (startMinRad > startMaxRad){
			temp = startMinRad;
			startMinRad = startMaxRad;
			startMaxRad = temp;
		}
		
		if (startMinEgg> startMaxEgg){
			temp = startMinEgg;
			startMinEgg= startMaxEgg;
			startMaxEgg= temp;
		}
		
		if (startMinEnergy > startMaxEnergy){
			temp2 = startMinEnergy;
			startMinEnergy = startMaxEnergy;
			startMaxEnergy = temp2;
		}
		
		if (startMinMetabolism > startMaxMetabolism){
			temp2 = startMinMetabolism;
			startMinMetabolism = startMaxMetabolism;
			startMaxMetabolism = temp2;
		}
		
		if (startMinFood > startMaxFood){
			temp2 = startMinFood;
			startMinFood = startMaxFood;
			startMaxFood = temp2;
		}
		
		if (startMinSpeed < 2)
			startMinSpeed = 2;
		if (startMaxSpeed > 9)
			startMaxSpeed = 9;
		
		if (startMinRad < 80)
			startMinRad = 80;
		if (startMaxRad > 100)
			startMaxRad = 100;
		
		if (startMinEgg < 20000)
			startMinEgg = 20000;
		if (startMaxEgg > 40000)
			startMaxEgg = 40000;
		
		if (startMinEnergy < 6000.0)
			startMinEnergy = 6000.0;
		if (startMaxEnergy > 9000.0)
			startMaxEnergy = 9000.0;
		
		if (startMinMetabolism < 80.0)
			startMinMetabolism = 80.0;
		if (startMaxMetabolism > 120.0)
			startMaxMetabolism = 120.0;
		
		if (startMinFood < 200.0)
			startMinFood = 200.0;
		if (startMaxFood > 800.0)
			startMaxFood = 800.0;
			
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