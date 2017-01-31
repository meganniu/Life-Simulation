import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javafx.scene.media.AudioClip;

/**
 * Driver class of simulation
 */
public class Main extends JFrame implements KeyListener, ActionListener, ComponentListener {
	
	/**
	 * displayed when simulation starts
	 */
	JPanel gameScreen = new JPanel(new GridBagLayout());
	
	/**
	 * displayed when program begins, before simulation
	 */
	StartScreen startScreen = new StartScreen();

	/**
	 * Sandbox screen
	 */
	JFrame s = new JFrame();

	
	/**
	 * displayed when instructions requested
	 */
	InstructionScreen instructionScreen = new InstructionScreen();
	
	/**
	 * toggles sandbox mode
	 */
	JToggleButton sandbox = new JToggleButton("Sandbox Mode");
	
	/**
	 * displayed when user wished to set beginning stats
	 */
	GetPreferences getPreferences = new GetPreferences();

	
	/**
	 * if the simulation is running
	 */
	boolean gameStatus = false;
	boolean settingsOpen = false;
	
	/**
	 * if user wishes to add a carnivore
	 */
	static boolean addingCarnivore = false;
	
	/**
	 * if user wishes to add a herbivore
	 */
	static boolean addingHerbivore = false;

	/**
	 * true to start sim, false to pause sim
	 */
	boolean startSim;
	
	/**
	 * width and height of display area for simulation graphics
	 */
	private static int drawWidth = 600, drawHeight = 600;
	
	/**
	 * chasing time of organisms
	 */
	public static int chaseCD = 4000;
	
	/**
	 * rate at which food spawns
	 */
	public static int foodSpawnRate = 3;
	
	/**
	 * rate at which food decays
	 */
	public static double foodDecayRate = 1.0;
	
	/**
	 * rate at which energy decreases
	 */
	public static double energyDecayRate = 1.0;
	
	/**
	 * minimum energy required
	 */
	public static double energyReq = 6000.0;
	
	/**
	 * default energy of a newborn
	 */
	public static double newbornEnergy = 4000.0;
	
	/**
	 * maximum energy of an organism
	 */
	public static double maximumEnergy = 15000.0;
	
	/**
	 * hatch time of an egg
	 */
	public static long hatchTime = 10000;
	
	/**
	 * GamePane for controlling the passage of ticks and updating of simulation graphics
	 */
	GamePane gamePane;
	
	/**
	 * control panel to move around simulation graphics
	 */
	JButton up = new JButton("^");
	JButton right = new JButton(">");
	JButton down = new JButton("v");
	JButton left = new JButton("<");
	
	/**
	 * start button for simulation
	 * switched to pause button when simulation is running
	 */
	JButton go = new JButton("Start");
	static JButton startBtn = new JButton("Start");
	
	/**
	 * button to add carnivore
	 */
	static JButton addCarnivore = new JButton("Add Carnivore");
	
	/**
	 * button to add herbivore
	 */
	static JButton addHerbivore = new JButton("Add Herbivore");

	/**
	 * default starting stats
	 */
	int startingCarnivores = 3;
	int startingHerbivores = 20;
	int startMinSpeed = 2, startMaxSpeed = 9;
	int startMinRad = 80, startMaxRad = 150;
	int startMinEgg = 4000, startMaxEgg = 20000;
	double startMinEnergy = 6000.0, startMaxEnergy = 9000.0;
	double startMinMetabolism = 80.0, startMaxMetabolism = 120.0;
	double startMinFood = 200.0, startMaxFood = 800.0;
	long chaseLength = 15000;

	/**
	 * Display start screen
	 */
	public class StartScreen extends JPanel implements MouseMotionListener, MouseListener {
		/**
		 * tracks dynamic shifting of backmost image layer
		 */
		int shiftx = 500, shifty = 300;

		// Parallax factors (B = back, F = front, Btn = buttons --- X is x
		// factor, Y is y factor)

		// Background moves faster than foreground
		// double PBX = 10, PBY = 6, PFX = 20, PFY = 16, PBtnX = 13, PBtnY = 10;

		// Background moves slower than foreground
		double PBX = 20, PBY = 16, PFX = 10, PFY = 6, PBtnX = 13, PBtnY = 10;

		/**
		 * images to be displayed
		 */
		BufferedImage front = null, frontSel = null, btn1 = null, btn1Sel = null, btn2 = null, btn2Sel = null;
		BufferedImage back = null;

		/**
		 * get which areas are selected
		 */
		boolean selected1, selected2, selected3;

		/**
		 * audio effect for when mouse is dragged across component
		 */
		AudioClip click = new AudioClip(new File("sounds/click.wav").toURI().toString());

		/**
		 * areas that subsitute buttons
		 */
		Shape r1 = new Ellipse2D.Double((getWidth() - 730) / 2 - (int) ((shiftx - 500) / PFX) + 175 + 12,
				(getHeight() - 420) / 2 - (int) ((shifty - 400) / PFY) - 75 + 32, 355, 355);
		Shape r2 = new Ellipse2D.Double(getWidth() / 2 - 250 - 50 - (int) ((shiftx - 500) / PBtnX) + 63,
				10 + 400 - (int) ((shifty - 400) / PBtnY), 125, 125);
		Shape r3 = new Ellipse2D.Double(getWidth() / 2 + 50 - (int) ((shiftx - 500) / PBtnX) + 63,
				10 + 400 - (int) ((shifty - 400) / PBtnY), 125, 125);

		/**
		 * StartScreen constructor
		 */
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

		/**
		 * Update graphics of start screen
		 */
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

		/**
		 * Detects movement of mouse and mouse clicks
		 */
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

		/**
		 * Detects movement of mouse and mouse clicks
		 */
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

		/**
		 * Detects mouse clicks and loads different screens accordingly
		 */
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

		/**
		 * Detects mouse clicks and loads different screens accordingly
		 */		
		public void mousePressed(MouseEvent e) {
			if (r1.contains(e.getPoint()))
				generateGame();
			else if (r2.contains(e.getPoint()))
				instructions(shifty);
			else if (r3.contains(e.getPoint()))
				getPreferences();
		}

		/**
		 * Detects mouse clicks and loads different screens accordingly
		 */
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

	/**
	 * Displays instruction screen
	 */
	public class InstructionScreen extends JPanel implements MouseMotionListener, MouseListener {

		int shifty = 420;

		BufferedImage front = null, back = null;

		/**
		 * Instruction screen constructor
		 */
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

		/**
		 * Display graphics of instruction screen
		 */
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
		
		/**
		 * Leave instructions if mouse is pressed
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			leaveInstructions();

		}

		/**
		 * Leave instructions if mouse is pressed
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			leaveInstructions();

		}

		/**
		 * Scroll down instructions when mouse is moved
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			shifty = e.getY();
			repaint();
		}

		/**
		 * Scroll down instructions when mouse is moved
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			shifty = e.getY();
			repaint();

		}

		/**
		 * Scroll down instructions when mouse is moved
		 * @param shifty to start display of instruction image in respect to y axis
		 */
		public void setShiftY(int shifty) {
			this.shifty = shifty;

		}

	}
	/**
	 * Panel on which to display stats of objects
	 */
	static StatsPanel statsPanel = new StatsPanel();

	/**
	 * Main constructor
	 */
	public Main() {

		addKeyListener(this);
		addComponentListener(this);
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

	/**
	 * Receive user inputs to scroll across simulation drawing 
	 */
	public void actionPerformed(ActionEvent e) {
		if (gameStatus) {
			if (e.getSource() == go) {
				if (startSim == false) {
					go.setText("Stop");
					gamePane.start();
				} else {
					go.setText("Start");
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
			if (e.getSource() == addCarnivore) {
				addingCarnivore = !addingCarnivore;
				if(addingCarnivore){
					addHerbivore.setEnabled(false);
					addingHerbivore = false;
				}
				else{
					addHerbivore.setEnabled(true);
				}
			}
			if (e.getSource() == addHerbivore) {
				addingHerbivore = !addingHerbivore;
				if(addingHerbivore){
					addCarnivore.setEnabled(false);
					addingCarnivore = false;
				}
				else{
					addCarnivore.setEnabled(true);
				}
			}
			if (!GamePane.running && gameStatus)
				gamePane.render();
		}
		requestFocus();
	}

	/**
	 * Display instruction screen
	 * @param shifty section of instructions to display from
	 */
	public void instructions(int shifty) {
		instructionScreen.setShiftY(shifty);
		setContentPane(instructionScreen);
		revalidate();
	}

	/**
	 * Leave instructions screen
	 */
	public void leaveInstructions() {
		setContentPane(startScreen);
		revalidate();
	}

	/**
	 * Get starting stats from user
	 */
	public class GetPreferences extends JFrame implements WindowListener {
		/**
		 * set beginning stats
		 */
		JButton set = new JButton("Set");
		
		/**
		 * cancel begining stats
		 */
		JButton cancel = new JButton("Cancel");

		/**
		 * stat labels
		 */
		JLabel carnivoresLbl = new JLabel("Carnivores");
		JLabel minSpeedLbl = new JLabel("Minimum Starting Speed");
		JLabel minRadLbl = new JLabel("Minimum Starting Detect Radius");
		JLabel minEggLbl = new JLabel("Minimum Starting Egg Cycle Length");
		JLabel minEnergyLbl = new JLabel("Minimum Starting Energy");
		JLabel minMetabolismLbl = new JLabel("Minimum Starting Metabolism");

		JLabel herbivoresLbl = new JLabel("Herbivores");
		JLabel maxSpeedLbl = new JLabel("Maximum Starting Speed");
		JLabel maxRadLbl = new JLabel("Maximum Starting Detect Radius");
		JLabel maxEggLbl = new JLabel("Maximum Starting Egg Cycle Length");
		JLabel maxEnergyLbl = new JLabel("Maximum Starting Energy");
		JLabel maxMetabolismLbl = new JLabel("Maximum Starting Metabolism");

		JLabel minFoodLbl = new JLabel("Minimum Food nutrition");
		JLabel maxFoodLbl = new JLabel("Maximum Food nutrition");

		JLabel chaseLbl = new JLabel("Starting chase length");
		JLabel chaseCdLbl = new JLabel("Chase cooldown length");
		JLabel foodSpawnLbl = new JLabel("Food spawn rate");
		JLabel foodDecayLbl = new JLabel("Food decay rate");
		JLabel energyDecayLbl = new JLabel("Energy decay rate");
		JLabel eggReqLbl = new JLabel("Energy required for an egg");
		JLabel eggHatchEnergyLbl = new JLabel("Newborn Starting Energy");
		JLabel maximumEnergyLbl = new JLabel("Maximum possible energy");
		JLabel eggHatchLbl = new JLabel("Egg hatch time");

		/**
		 * stat text fields
		 */
		JTextField carnivoresTF = new JTextField();
		JTextField minSpeedTF = new JTextField();
		JTextField minRadTF = new JTextField();
		JTextField minEggTF = new JTextField();
		JTextField minEnergyTF = new JTextField();
		JTextField minMetabolismTF = new JTextField();
		JTextField minFoodTF = new JTextField();
		JTextField minChaseTF = new JTextField();

		JTextField herbivoresTF = new JTextField();
		JTextField maxSpeedTF = new JTextField();
		JTextField maxRadTF = new JTextField();
		JTextField maxEggTF = new JTextField();
		JTextField maxEnergyTF = new JTextField();
		JTextField maxMetabolismTF = new JTextField();
		JTextField maxFoodTF = new JTextField();
		JTextField maxChaseTF = new JTextField();

		JTextField chaseTF = new JTextField();
		JTextField chaseCdTF = new JTextField();
		JTextField foodSpawnTF = new JTextField();
		JTextField foodDecayTF = new JTextField();
		JTextField energyDecayTF = new JTextField();
		JTextField eggReqTF = new JTextField();
		JTextField eggHatchEnergyTF = new JTextField();
		JTextField maximumEnergyTF = new JTextField();
		JTextField eggHatchTF = new JTextField();

		/**
		 * GetPreferences constructor
		 */
		public GetPreferences() {

			System.out.println("Created settings");

			setTitle("Customization");
			setSize(650, 800);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

			gbc.gridy = 14;
			add(chaseLbl, gbc);

			gbc.gridy = 16;
			add(foodSpawnLbl, gbc);

			gbc.gridy = 18;
			add(energyDecayLbl, gbc);

			gbc.gridy = 20;
			add(eggHatchEnergyLbl, gbc);

			gbc.gridy = 22;
			add(eggHatchLbl, gbc);

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

			gbc.gridy = 14;
			add(chaseCdLbl, gbc);

			gbc.gridy = 16;
			add(foodDecayLbl, gbc);

			gbc.gridy = 18;
			add(eggReqLbl, gbc);

			gbc.gridy = 20;
			add(maximumEnergyLbl, gbc);

			gbc.gridheight = 2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.gridy = 22;
			add(sandbox, gbc);

			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.NONE;

			gbc.anchor = GridBagConstraints.PAGE_START;
			GhostText ghostTextC = new GhostText(carnivoresTF, "Carnivores to start");
			GhostText ghostTextMinSpeed = new GhostText(minSpeedTF, "Default 2");
			GhostText ghostTextMinRad = new GhostText(minRadTF, "Default 80");
			GhostText ghostTextMinEgg = new GhostText(minEggTF, "Default 4000");
			GhostText ghostTextMinEnergy = new GhostText(minEnergyTF, "Default 6000.0");
			GhostText ghostTextMinMetabolism = new GhostText(minMetabolismTF, "Default 80.0");
			GhostText ghostTextMinFood = new GhostText(minFoodTF, "Default 200.0");

			GhostText ghostTextH = new GhostText(herbivoresTF, "Herbivores to start");
			GhostText ghostTextMaxSpeed = new GhostText(maxSpeedTF, "Default 9");
			GhostText ghostTextMaxRad = new GhostText(maxRadTF, "Default 150");
			GhostText ghostTextMaxEgg = new GhostText(maxEggTF, "Default 40000");
			GhostText ghostTextMaxEnergy = new GhostText(maxEnergyTF, "Default 9000.0");
			GhostText ghostTextMaxMetabolism = new GhostText(maxMetabolismTF, "Default 120.0");
			GhostText ghostTextMaxFood = new GhostText(maxFoodTF, "Default 800.0");

			GhostText ghostTextChase = new GhostText(chaseTF, "Default 15000, range 5000 - 20000");
			GhostText ghostTextChaseCd = new GhostText(chaseCdTF, "Default 4000, range 2000 - 6000");
			GhostText ghostTextFoodSpawn = new GhostText(foodSpawnTF, "Default 3, range 0 - 9");
			GhostText ghostTextFoodDecay = new GhostText(foodDecayTF, "Default 1.0, range 0.0 - 4.0");
			GhostText ghostTextEnergyDecay = new GhostText(energyDecayTF, "Default 1.0, range 0.0 - 4.0");
			GhostText ghostTextEggReq = new GhostText(eggReqTF, "Default 6000.0, range 2000.0 - Maximum Energy");
			GhostText ghostTextEggHatchEnergy = new GhostText(eggHatchEnergyTF,
					"Default 4000.0, range 0.0 - Maximum Energy");
			GhostText ghostTextMaximumEnergy = new GhostText(maximumEnergyTF,
					"Default 15000.0, range 2000.0 - 30000.0");
			GhostText ghostTextEggHatch = new GhostText(eggHatchTF, "Default 10000.0, range 2000.0 - 30000.0");

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 0.7;
			gbc.insets = new Insets(0, 0, 10, 10);
			carnivoresTF.setPreferredSize(new Dimension(300, 20));
			add(carnivoresTF, gbc);

			gbc.gridy = 3;
			minSpeedTF.setPreferredSize(new Dimension(300, 20));
			add(minSpeedTF, gbc);

			gbc.gridy = 5;
			minRadTF.setPreferredSize(new Dimension(300, 20));
			add(minRadTF, gbc);

			gbc.gridy = 7;
			minEggTF.setPreferredSize(new Dimension(300, 20));
			add(minEggTF, gbc);

			gbc.gridy = 9;
			minEnergyTF.setPreferredSize(new Dimension(300, 20));
			add(minEnergyTF, gbc);

			gbc.gridy = 11;
			minMetabolismTF.setPreferredSize(new Dimension(300, 20));
			add(minMetabolismTF, gbc);

			gbc.gridy = 13;
			minFoodTF.setPreferredSize(new Dimension(300, 20));
			add(minFoodTF, gbc);

			gbc.gridy = 15;
			chaseTF.setPreferredSize(new Dimension(300, 20));
			add(chaseTF, gbc);

			gbc.gridy = 17;
			foodSpawnTF.setPreferredSize(new Dimension(300, 20));
			add(foodSpawnTF, gbc);

			gbc.gridy = 19;
			energyDecayTF.setPreferredSize(new Dimension(300, 20));
			add(energyDecayTF, gbc);

			gbc.gridy = 21;
			eggHatchEnergyTF.setPreferredSize(new Dimension(300, 20));
			add(eggHatchEnergyTF, gbc);

			gbc.gridy = 23;
			eggHatchTF.setPreferredSize(new Dimension(300, 20));
			add(eggHatchTF, gbc);

			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 10, 10, 0);
			herbivoresTF.setPreferredSize(new Dimension(300, 20));
			add(herbivoresTF, gbc);

			gbc.gridy = 3;
			maxSpeedTF.setPreferredSize(new Dimension(300, 20));
			add(maxSpeedTF, gbc);

			gbc.gridy = 5;
			maxRadTF.setPreferredSize(new Dimension(300, 20));
			add(maxRadTF, gbc);

			gbc.gridy = 7;
			maxEggTF.setPreferredSize(new Dimension(300, 20));
			add(maxEggTF, gbc);

			gbc.gridy = 9;
			maxEnergyTF.setPreferredSize(new Dimension(300, 20));
			add(maxEnergyTF, gbc);

			gbc.gridy = 11;
			maxMetabolismTF.setPreferredSize(new Dimension(300, 20));
			add(maxMetabolismTF, gbc);

			gbc.gridy = 13;
			maxFoodTF.setPreferredSize(new Dimension(300, 20));
			add(maxFoodTF, gbc);

			gbc.gridy = 15;
			chaseCdTF.setPreferredSize(new Dimension(300, 20));
			add(chaseCdTF, gbc);

			gbc.gridy = 17;
			foodDecayTF.setPreferredSize(new Dimension(300, 20));
			add(foodDecayTF, gbc);

			gbc.gridy = 19;
			eggReqTF.setPreferredSize(new Dimension(300, 20));
			add(eggReqTF, gbc);

			gbc.gridy = 21;
			maximumEnergyTF.setPreferredSize(new Dimension(300, 20));
			add(maximumEnergyTF, gbc);

			cancel.addActionListener(new MouseListener());
			set.addActionListener(new MouseListener());

			cancel.setPreferredSize(new Dimension(80, 30));
			set.setPreferredSize(new Dimension(80, 30));

			gbc.gridx = 0;
			gbc.gridy = 24;
			gbc.weighty = 0.3;
			add(cancel, gbc);

			gbc.gridx = 1;
			add(set, gbc);
		}

		/**
		 * Read in user input if user "sets" stats
		 */
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
					} catch (NumberFormatException ev) {
					}

					try {
						startMinSpeed = Integer.parseInt(minSpeedTF.getText());
					} catch (NumberFormatException ev) {
					}

					try {
						startMaxSpeed = Integer.parseInt(maxSpeedTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						startMinRad = Integer.parseInt(minRadTF.getText());
					} catch (NumberFormatException ev) {
					}

					try {
						startMaxRad = Integer.parseInt(maxRadTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						startMinEgg = Integer.parseInt(minEggTF.getText());
					} catch (NumberFormatException ev) {
					}

					try {
						startMaxEgg = Integer.parseInt(maxEggTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						startMinEnergy = Integer.parseInt(minEnergyTF.getText());
					} catch (NumberFormatException ev) {
					}

					try {
						startMaxEnergy = Integer.parseInt(maxEnergyTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						startMinMetabolism = Integer.parseInt(minMetabolismTF.getText());
					} catch (NumberFormatException ev) {
					}

					try {
						startMaxMetabolism = Integer.parseInt(maxMetabolismTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						startMinFood = Integer.parseInt(minFoodTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						startMaxFood = Integer.parseInt(maxFoodTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						chaseLength = Integer.parseInt(chaseTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						chaseCD = Integer.parseInt(chaseCdTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						foodSpawnRate = Integer.parseInt(foodSpawnTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						foodDecayRate = Integer.parseInt(foodDecayTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						energyDecayRate = Integer.parseInt(energyDecayTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						energyReq = Integer.parseInt(eggReqTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						newbornEnergy = Integer.parseInt(eggHatchEnergyTF.getText());
					} catch (NumberFormatException ev) {
					}
					try {
						hatchTime = Integer.parseInt(eggHatchTF.getText());
					} catch (NumberFormatException ev) {
					}
					setVisible(false);
					settingsOpen = false;
				}
			}
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosed(WindowEvent e) {
			setVisible(false);
			settingsOpen = false;
		}

		@Override
		public void windowClosing(WindowEvent e) {
			setVisible(false);
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

	/**
	 * Get user preferences 
	 */
	public void getPreferences() {
		System.out.println(settingsOpen);
		if (!settingsOpen) {
			getPreferences.setVisible(true);
			settingsOpen = true;
		}
	}

	/**
	 * Populate game pane with components necessary to display simulation
	 */
	public void generateGame() {

		sort();
		gamePane = new GamePane(drawWidth, drawHeight, startingCarnivores, startingHerbivores, startMinSpeed,
				startMaxSpeed, startMinRad, startMaxRad, startMinEgg, startMaxEgg, startMinEnergy, startMaxEnergy,
				startMinMetabolism, startMaxMetabolism, startMinFood, startMaxFood, chaseLength);
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
		gbc.gridy = 1;
		gameScreen.add(statsPanel, gbc);

		gbc.gridheight = 3;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gameScreen.add(gamePane, gbc);

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

		addCarnivore.addActionListener(this);
		addHerbivore.addActionListener(this);

		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

		go.setAlignmentX(Component.CENTER_ALIGNMENT);
		controlPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		addCarnivore.setAlignmentX(Component.CENTER_ALIGNMENT);
		addHerbivore.setAlignmentX(Component.CENTER_ALIGNMENT);

		sidePanel.add(go);
		sidePanel.add(controlPanel);
		sidePanel.add(addCarnivore);
		sidePanel.add(addHerbivore);

		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gameScreen.add(sidePanel, gbc);
		
		addCarnivore.setIcon(new ImageIcon(DrawArea.cImg));
		addHerbivore.setIcon(new ImageIcon(DrawArea.hImg));

		setContentPane(gameScreen);
		revalidate();
		pack();
		gameStatus = true;

		createSandbox();
	}

	static JToggleButton addC;
	static JToggleButton addH;
	static JToggleButton addE;
	static JToggleButton addF;
	JButton clear = new JButton("Clear Board");
	JComponent[][] optionsGrid = null;
	JPanel options = new JPanel();

	public void createSandbox() {
		
		addC = new JToggleButton("Add Carnivore", new ImageIcon(DrawArea.cImg.getScaledInstance(16, 16, Image.SCALE_FAST)));
		addH = new JToggleButton("Add Herbivore",new ImageIcon(DrawArea.hImg.getScaledInstance(16, 16, Image.SCALE_FAST)));
		addE = new JToggleButton("Add Egg",new ImageIcon(DrawArea.eImg.getScaledInstance(16, 16, Image.SCALE_FAST)));
		addF = new JToggleButton("Add Food",new ImageIcon(DrawArea.fImg.getScaledInstance(16, 16, Image.SCALE_FAST)));
		
		 
		
		addC.addActionListener(sandboxListener);
		addH.addActionListener(sandboxListener);
		addE.addActionListener(sandboxListener);
		addF.addActionListener(sandboxListener);
		clear.addActionListener(sandboxListener);
		
		if (!sandbox.isSelected()) {
			s.setTitle("Sandbox Options");
			s.addComponentListener(this);
			s.setLayout(new GridBagLayout());
			s.setSize(300, this.getHeight());
			s.setVisible(true);
			s.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			s.setLocation(this.getX() + this.getWidth(), this.getY());

			GridBagConstraints c = new GridBagConstraints();

			c.insets = new Insets(2, 2, 2, 2);
			c.fill = GridBagConstraints.BOTH;
			c.gridwidth = 2;
			s.add(new JLabel("Options"), c);
			c.gridy=1;
			s.add(clear, c);
			c.gridwidth=1;
			c.gridy = 2;
			s.add(addC, c);
			c.gridx=1;
			s.add(addH, c);
			c.gridy = 4;
			c.gridx=0;
			s.add(addE, c);
			c.gridx=1;
			s.add(addF, c);
			c.gridwidth=2;
			c.gridy=5;
			s.add(options, c);
			
		}
	}

	ActionListener sandboxListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			
			if(e.getSource() == clear){
				DrawArea.herbivores.clear();
				DrawArea.carnivores.clear();
				DrawArea.food.clear();
				DrawArea.eggs.clear();
				gamePane.render();
				return;
			}
			
			if (!addC.isSelected() && !addH.isSelected() && !addE.isSelected() && !addF.isSelected()) {
				s.remove(options);
			}
			if (src instanceof JToggleButton) {
				
				GridBagConstraints c = new GridBagConstraints();
				
				deselectButtons((JToggleButton) src);
				optionsGrid = gamePane.generatePanel(((JToggleButton) src).getText());
				s.remove(options);
				options = new JPanel(new GridLayout(optionsGrid.length, optionsGrid[0].length));
				for(int i = 0; i < optionsGrid.length; i++)
					for(int j = 0; j < optionsGrid[0].length; j++){
						options.add(optionsGrid[i][j]);
					}
				c.insets = new Insets(2, 2, 2, 2);
				c.fill = GridBagConstraints.BOTH;
				c.gridwidth=2;
				c.gridy=5;
				s.add(options, c);
				s.revalidate();
			}
		}

	};

	public void deselectButtons(JToggleButton jtb) {
		if (addC != jtb)
			addC.setSelected(false);
		if (addH != jtb)
			addH.setSelected(false);
		if (addE != jtb)
			addE.setSelected(false);
		if (addF != jtb)
			addF.setSelected(false);
	}

	/**
	 * check that starting stats entered by user are valid
	 */
	public void sort() {
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

		if (startMinSpeed > startMaxSpeed) {
			temp = startMinSpeed;
			startMinSpeed = startMaxSpeed;
			startMaxSpeed = temp;
		}

		if (startMinRad > startMaxRad) {
			temp = startMinRad;
			startMinRad = startMaxRad;
			startMaxRad = temp;
		}

		if (startMinEgg > startMaxEgg) {
			temp = startMinEgg;
			startMinEgg = startMaxEgg;
			startMaxEgg = temp;
		}

		if (startMinEnergy > startMaxEnergy) {
			temp2 = startMinEnergy;
			startMinEnergy = startMaxEnergy;
			startMaxEnergy = temp2;
		}

		if (startMinMetabolism > startMaxMetabolism) {
			temp2 = startMinMetabolism;
			startMinMetabolism = startMaxMetabolism;
			startMaxMetabolism = temp2;
		}

		if (startMinFood > startMaxFood) {
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
		if (startMaxRad > 200)
			startMaxRad = 200;

		if (startMinEgg < 4000)
			startMinEgg = 4000;
		if (startMaxEgg > 20000)
			startMaxEgg = 20000;

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

		if (chaseLength < 5000)
			chaseLength = 5000;
		else if (chaseLength > 20000)
			chaseLength = 20000;

		if (chaseCD < 2000)
			chaseCD = 2000;
		else if (chaseCD > 6000)
			chaseCD = 6000;

		if (foodSpawnRate < 0)
			foodSpawnRate = 0;
		else if (foodSpawnRate > 9)
			foodSpawnRate = 9;

		if (foodDecayRate < 0.0)
			foodDecayRate = 0.0;
		else if (foodDecayRate > 4.0)
			foodDecayRate = 4.0;

		if (energyDecayRate < 0.0)
			energyDecayRate = 0.0;
		else if (energyDecayRate > 4.0)
			energyDecayRate = 4.0;

		if (maximumEnergy < 2000.0)
			maximumEnergy = 2000.0;
		else if (maximumEnergy > 30000.0)
			maximumEnergy = 30000.0;

		if (energyReq < 0.0)
			energyReq = 0.0;
		else if (energyReq > maximumEnergy)
			energyReq = maximumEnergy;

		if (newbornEnergy < 2000.0)
			newbornEnergy = 2000.0;
		else if (newbornEnergy > maximumEnergy)
			newbornEnergy = maximumEnergy;

		if (hatchTime < 2000)
			hatchTime = 2000;
		else if (hatchTime > 30000)
			hatchTime = 30000;
	}

	/**
	 * Alternative keyboard controls for moving around simulation display
	 */
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

	public static void main(String[] args) {
		Main simulation = new Main();

	}

	/**
	 * GhostText implemented in textfields of preferences menu
	 * Code taken from http://stackoverflow.com/questions/10506789/how-to-display-faint-gray-ghost-text-in-a-jtextfield
	 */
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

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		if (e.getSource() == this) {
			s.setLocation(this.getX() + this.getWidth(), this.getY());
		} else {
			this.setLocation(s.getX() - this.getWidth(), s.getY());
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
}