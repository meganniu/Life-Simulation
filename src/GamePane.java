import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 * Control ticks and actions performed each tick
 */
public class GamePane extends Canvas implements MouseListener, Runnable {

	/**
	 * is the simulation running
	 */
	static boolean running = false;

	/**
	 * thread on which simulation is running
	 */
	private Thread thread;

	/**
	 * tick count per second
	 */
	private int tickCount;

	/**
	 * frame count per second
	 */
	private int frameCount;

	/**
	 * region on which to draw the simulation
	 */
	static Rectangle drawRegion;

	/**
	 * time elapsed in milliseconds
	 */
	static long timeElapsed = 0;

	/**
	 * time to add to timeElapsed
	 */
	private long timeToAdd = 0;

	/**
	 * ticks elapsed
	 */
	static long tickCounter = 0;

	/**
	 * width of drawRegion
	 */
	int width;

	/**
	 * height of drawRegion
	 */
	int height;

	/**
	 * conditions set force by user automatically generated if not set
	 */
	int startingCarnivores, startingHerbivores;
	int startMinSpeed, startMaxSpeed;
	int startMinRad, startMaxRad;
	int startMinEgg, startMaxEgg;
	double startMinEnergy, startMaxEnergy;
	double startMinMetabolism, startMaxMetabolism;
	double startMinFood, startMaxFood;
	long chaseLength;
	private DrawArea drawArea;

	/**
	 * last organism remaining
	 */
	Organism finalOrg;

	/**
	 * GamePane constructor
	 * 
	 * @param width
	 *            width of drawRegion
	 * @param height
	 *            height of drawRegion
	 * @param startingCarnivores
	 *            amount of carnivores to start
	 * @param startingHerbivores
	 *            amount of herbivores to start
	 * @param startMinSpeed
	 *            minimum starting speed
	 * @param startMaxSpeed
	 *            maximum starting speed
	 * @param startMinRad
	 *            minimum starting radius of detection
	 * @param startMaxRad
	 *            maximum starting radius of detection
	 * @param startMinEgg
	 *            minimum starting amount of eggs
	 * @param startMaxEgg
	 *            maximum starting amount of eggs
	 * @param startMinEnergy
	 *            minimum starting amount of energy
	 * @param startMaxEnergy
	 *            maximum starting amount of energy
	 * @param startMinMetabolism
	 *            minimum starting metabolism speed
	 * @param startMaxMetabolism
	 *            maximum starting matabolism speed
	 * @param startMinFood
	 *            minimum amount of starting food
	 * @param startMaxFood
	 *            maximum amount of starting food
	 * @param chaseLength
	 *            chase length for all organisms
	 */
	public GamePane(int width, int height, int startingCarnivores, int startingHerbivores, int startMinSpeed,
			int startMaxSpeed, int startMinRad, int startMaxRad, int startMinEgg, int startMaxEgg,
			double startMinEnergy, double startMaxEnergy, double startMinMetabolism, double startMaxMetabolism,
			double startMinFood, double startMaxFood, long chaseLength) {
		this.startingCarnivores = startingCarnivores;
		this.startingHerbivores = startingHerbivores;

		this.startMinSpeed = startMinSpeed;
		this.startMaxSpeed = startMaxSpeed;

		this.startMinRad = startMinRad;
		this.startMaxRad = startMaxRad;

		this.startMinEgg = startMinEgg;
		this.startMaxEgg = startMaxEgg;

		this.startMinEnergy = startMinEnergy;
		this.startMaxEnergy = startMaxEnergy;

		this.startMinMetabolism = startMinMetabolism;
		this.startMaxMetabolism = startMaxMetabolism;

		this.startMinFood = startMinFood;
		this.startMaxFood = startMaxFood;

		this.chaseLength = chaseLength;

		drawArea = new DrawArea(startingCarnivores, startingHerbivores, startMinSpeed, startMaxSpeed, startMinRad,
				startMaxRad, startMinEgg, startMaxEgg, startMinEnergy, startMaxEnergy, startMinMetabolism,
				startMaxMetabolism, startMinFood, startMaxFood, chaseLength);

		this.width = width;
		this.height = height;

		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(width, height)); // size

	}

	/**
	 * Start or resume simulation
	 */
	public synchronized void start() {
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * End or pause simulation
	 */
	public synchronized void stop() {
		if (!running)
			return;
		timeToAdd = timeElapsed;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Accumulates counters concerning ticks and time
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		long start = System.currentTimeMillis();
		double delta = 0.0;
		int ticksPerSecond = 30;
		double ns = 1000000000.0 / ticksPerSecond;
		int frames = 0;
		int ticks = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				ticks++;
				tickCounter++;
				delta--;
				timeElapsed = timeToAdd + System.currentTimeMillis() - start;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frameCount = frames;
				tickCount = ticks;
				frames = 0;
				ticks = 0;
			}
		}
	}

	/**
	 * Actions performed every tick, updates the data of the simulation
	 */
	public void tick() { // Per tick
		drawArea.updatePositions();
		drawArea.decayFood();
		drawArea.eat();
		drawArea.spawnFood();
		drawArea.layEggs();
		drawArea.hatchEggs();
		drawArea.energyCheck();
		drawArea.eatCheck();
		System.out.println(Main.sandboxOn);
		if ((finalOrg = drawArea.checkEnd()) != null && timeElapsed > 10 && !Main.sandboxOn) {
			Main.go.setEnabled(false);
			Main.addCarnivore.setEnabled(false);
			Main.addHerbivore.setEnabled(false);
			render();
			stop();
		}
		Main.statsPanel.updateStats();
		Main.overviewPnl.update();
	}

	/**
	 * Updates the graphics of the simulation
	 */
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		drawArea.updateImage();
		Graphics g = bs.getDrawGraphics();
		g.drawImage(drawArea.getSubimage(drawRegion.x, drawRegion.y, drawRegion.width, drawRegion.height), 0, 0,
				drawRegion.width / 2, drawRegion.height / 2, null);
		g.drawString("FPS: " + frameCount + " | Ticks: " + tickCount + " | Time Elapsed: "
				+ new DecimalFormat("#.###").format(timeElapsed / 1000.0) + "s", 5, 15);

		if (finalOrg != null && !Main.sandboxOn) {
			g.setColor(Color.white);
			g.fillRect((450) / 2, 150 / 2, 150, 250);

			g.setColor(Color.black);
			g.drawRect((450) / 2, 150 / 2, 150, 250);

			g.drawString("Simulation Over!", (500) / 2 + 6, (height - 400) / 2 + 10);
			g.drawString("Only one organism left", (500) / 2 - 12, (height - 400) / 2 + 25);
			ArrayList<String> rawStats = finalOrg.getFinalStats();

			for (int i = 0; i < rawStats.size(); i++) {
				g.drawString(rawStats.get(i), (500) / 2 - 12, (height - 400) / 2 + (40 + 15 * i));
			}
		}

		g.dispose();
		bs.show();
	}

	/**
	 * Updating the graphics
	 */
	public void paint(Graphics g) {
		render();
	}

	/**
	 * Detect when object of simulation is selected by player
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		boolean orgFound = false;
		boolean eggFound = false;
		boolean foodFound = false;
		int x = 2 * e.getX() + drawRegion.x;
		int y = 2 * e.getY() + drawRegion.y;

		if (Main.addBtn.isSelected()) {
			return;
		}
		for (int i = 0; i < DrawArea.carnivores.size() && !orgFound; i++) {
			if (DrawArea.carnivores.get(i).getHitbox().contains(x, y)) {
				StatsPanel.selectedOrg = DrawArea.carnivores.get(i);
				orgFound = true;
			}
		}
		for (int i = 0; i < DrawArea.herbivores.size() && !orgFound; i++) {
			if (DrawArea.herbivores.get(i).getHitbox().contains(x, y)) {
				StatsPanel.selectedOrg = DrawArea.herbivores.get(i);
				orgFound = true;
			}
		}
		for (int i = 0; i < DrawArea.eggs.size() && !eggFound; i++) {
			if (DrawArea.eggs.get(i).getHitbox().contains(x, y)) {
				StatsPanel.selectedEgg = DrawArea.eggs.get(i);
				eggFound = true;
			}
		}
		for (int i = 0; i < DrawArea.food.size() && !foodFound; i++) {
			if (DrawArea.food.get(i).hitbox.contains(x, y)) {
				StatsPanel.selectedFood = DrawArea.food.get(i);
				foodFound = true;
			}
		}

		if (orgFound) {
			StatsPanel.selectedEgg = null;
			StatsPanel.selectedFood = null;
		} else if (eggFound) {
			StatsPanel.selectedOrg = null;
			StatsPanel.selectedFood = null;
		} else if (foodFound) {
			StatsPanel.selectedOrg = null;
			StatsPanel.selectedEgg = null;
		} else {
			StatsPanel.selectedOrg = null;
			StatsPanel.selectedEgg = null;
			StatsPanel.selectedFood = null;
		}
		Main.statsPanel.updateStats();

		if (Main.addingCarnivore) {
			DrawArea.addCarnivore(new Point(x, y));
			Main.addingCarnivore = false;
			Main.addHerbivore.setEnabled(true);
		}
		if (Main.addingHerbivore) {
			DrawArea.addHerbivore(new Point(x, y));
			Main.addingHerbivore = false;
			Main.addCarnivore.setEnabled(true);
		}

		render();
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
		int x = 2 * e.getX() + drawRegion.x;
		int y = 2 * e.getY() + drawRegion.y;

		if (Main.addBtn.isSelected()) {
			if (sandbox.length == 2)
				DrawArea.food.add(new Food(new Point(x, y), (int) ((JSpinner) sandbox[1][0]).getValue()));
			else if (sandbox.length == 14) {
				int[] values = new int[7];
				for (int i = 1; i <= 13; i += 2) {
					values[i / 2] = (int) ((JSpinner) sandbox[i][0]).getValue();
				}
				DrawArea.herbivores.add(new Herbivore(1, new Point(x, y), values[0], values[1], values[2], values[3],
						values[4], values[5], values[6]));
			} else if (sandbox.length == 16) {
				int[] values = new int[8];
				for (int i = 1; i <= 15; i += 2) {
					values[i / 2] = (int) ((JSpinner) sandbox[i][0]).getValue();
				}
				DrawArea.carnivores.add(new Carnivore(1, new Point(x, y), (int) values[0], (int) values[1],
						(int) values[2], (int) values[3], (int) values[4], (double) values[5], (double) values[6],
						(long) values[7]));
			} else if (sandbox.length == 15) {
				int[] values = new int[7];
				for (int i = 1; i <= 13; i += 2) {
					values[i / 2] = (int) ((JSpinner) sandbox[i][0]).getValue();
				}
				DrawArea.eggs.add(new Egg(1, new Point(x, y), (int) values[0], (int) values[1], (int) values[2],
						(int) values[3], (int) values[4], (double) values[5], (long) values[6]));
			}
			render();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	JComponent[][] sandbox;

	public JComponent[][] generatePanel(String text) {
		JComponent[][] temp = null;
		if (text.equals("Add Carnivore")) {
			temp = new JComponent[16][2];
			temp[0][0] = new JLabel("Angle", JLabel.CENTER);
			temp[0][1] = new JLabel();
			temp[1][0] = new JSpinner(new SpinnerNumberModel(0, 0, 360, 30));
			temp[1][1] = new JButton("Random");
			temp[2][0] = new JLabel("Speed", JLabel.CENTER);
			temp[2][1] = new JLabel();
			temp[3][0] = new JSpinner(new SpinnerNumberModel(8, 1, 100, 1));
			temp[3][1] = new JButton("Random");
			temp[4][0] = new JLabel("D. Radius", JLabel.CENTER);
			temp[4][1] = new JLabel();
			temp[5][0] = new JSpinner(new SpinnerNumberModel(120, 60, 1000, 30));
			temp[5][1] = new JButton("Random");
			temp[6][0] = new JLabel("Egg Cycle", JLabel.CENTER);
			temp[6][1] = new JLabel();
			temp[7][0] = new JSpinner(new SpinnerNumberModel(5000, 2000, 80000, 500));
			temp[7][1] = new JButton("Random");
			temp[8][0] = new JLabel("Carnivorism", JLabel.CENTER);
			temp[8][1] = new JLabel();
			temp[9][0] = new JSpinner(new SpinnerNumberModel(10, 10, 15, 1));
			temp[9][1] = new JButton("Random");
			temp[10][0] = new JLabel("Energy", JLabel.CENTER);
			temp[10][1] = new JLabel();
			temp[11][0] = new JSpinner(new SpinnerNumberModel(7500, 1000, 15000, 500));
			temp[11][1] = new JButton("Random");
			temp[12][0] = new JLabel("Metabolism", JLabel.CENTER);
			temp[12][1] = new JLabel();
			temp[13][0] = new JSpinner(new SpinnerNumberModel(80, 40, 300, 15));
			temp[13][1] = new JButton("Random");
			temp[14][0] = new JLabel("Chase Length", JLabel.CENTER);
			temp[14][1] = new JLabel();
			temp[15][0] = new JSpinner(new SpinnerNumberModel(10000, 3000, 25000, 500));
			temp[15][1] = new JButton("Random");
		} else if (text.equals("Add Herbivore")) {
			temp = new JComponent[14][2];
			temp[0][0] = new JLabel("Angle", JLabel.CENTER);
			temp[0][1] = new JLabel();
			temp[1][0] = new JSpinner(new SpinnerNumberModel(0, 0, 360, 30));
			temp[1][1] = new JButton("Random");
			temp[2][0] = new JLabel("Speed", JLabel.CENTER);
			temp[2][1] = new JLabel();
			temp[3][0] = new JSpinner(new SpinnerNumberModel(8, 1, 100, 1));
			temp[3][1] = new JButton("Random");
			temp[4][0] = new JLabel("D. Radius", JLabel.CENTER);
			temp[4][1] = new JLabel();
			temp[5][0] = new JSpinner(new SpinnerNumberModel(120, 60, 1000, 30));
			temp[5][1] = new JButton("Random");
			temp[6][0] = new JLabel("Egg Cycle", JLabel.CENTER);
			temp[6][1] = new JLabel();
			temp[7][0] = new JSpinner(new SpinnerNumberModel(5000, 2000, 80000, 500));
			temp[7][1] = new JButton("Random");
			temp[8][0] = new JLabel("Carnivorism", JLabel.CENTER);
			temp[8][1] = new JLabel();
			temp[9][0] = new JSpinner(new SpinnerNumberModel(5, 1, 9, 1));
			temp[9][1] = new JButton("Random");
			temp[10][0] = new JLabel("Energy", JLabel.CENTER);
			temp[10][1] = new JLabel();
			temp[11][0] = new JSpinner(new SpinnerNumberModel(7500, 1000, 15000, 500));
			temp[11][1] = new JButton("Random");
			temp[12][0] = new JLabel("Metabolism", JLabel.CENTER);
			temp[12][1] = new JLabel();
			temp[13][0] = new JSpinner(new SpinnerNumberModel(80, 40, 300, 15));
			temp[13][1] = new JButton("Random");
		} else if (text.equals("Add Egg")) {
			temp = new JComponent[15][2];
			temp[0][0] = new JLabel("Angle", JLabel.CENTER);
			temp[0][1] = new JLabel();
			temp[1][0] = new JSpinner(new SpinnerNumberModel(0, 0, 360, 30));
			temp[1][1] = new JButton("Random");
			temp[2][0] = new JLabel("Speed", JLabel.CENTER);
			temp[2][1] = new JLabel();
			temp[3][0] = new JSpinner(new SpinnerNumberModel(8, 1, 100, 1));
			temp[3][1] = new JButton("Random");
			temp[4][0] = new JLabel("D. Radius", JLabel.CENTER);
			temp[4][1] = new JLabel();
			temp[5][0] = new JSpinner(new SpinnerNumberModel(120, 60, 1000, 30));
			temp[5][1] = new JButton("Random");
			temp[6][0] = new JLabel("Egg Cycle", JLabel.CENTER);
			temp[6][1] = new JLabel();
			temp[7][0] = new JSpinner(new SpinnerNumberModel(5000, 2000, 80000, 500));
			temp[7][1] = new JButton("Random");
			temp[8][0] = new JLabel("Carnivorism", JLabel.CENTER);
			temp[8][1] = new JLabel();
			temp[9][0] = new JSpinner(new SpinnerNumberModel(7, 1, 15, 1));
			temp[9][1] = new JButton("Random");
			temp[10][0] = new JLabel("Metabolism", JLabel.CENTER);
			temp[10][1] = new JLabel();
			temp[11][0] = new JSpinner(new SpinnerNumberModel(80, 40, 300, 15));
			temp[11][1] = new JButton("Random");
			temp[12][0] = new JLabel("Chase Length", JLabel.CENTER);
			temp[12][1] = new JLabel();
			temp[13][0] = new JSpinner(new SpinnerNumberModel(10000, 3000, 25000, 500));
			temp[13][1] = new JButton("Random");
		} else if (text.equals("Add Food")) {
			temp = new JComponent[2][2];
			temp[0][0] = new JLabel("Nutrition", JLabel.CENTER);
			temp[0][1] = new JLabel();
			temp[1][0] = new JSpinner(new SpinnerNumberModel(600, 400, 2000, 100));
			temp[1][1] = new JButton("Random");
		}

		sandbox = temp;
		return this.sandbox;
	}

	public ActionListener randomListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = 1;
			for (int i = 1; i <= 15; i += 2) {
				if (e.getSource() == sandbox[i][1]) {
					index = i;
					break;
				}
			}
			if (sandbox.length == 14 || sandbox.length == 16) {
				if (sandbox[index][0] instanceof JSpinner) {

					JSpinner js = ((JSpinner) sandbox[index][0]);
					if (index == 1)
						js.setValue((int)(Math.random() * (361)));
					else {
						SpinnerNumberModel nm = (SpinnerNumberModel) js.getModel();
						js.setValue((int) (Math.random() * ((int) nm.getMaximum() - (int) nm.getMinimum() + 1)
								+ (int) nm.getMinimum()));
					}
				}
			} else {
				if (sandbox[index][0] instanceof JSpinner) {
					JSpinner js = ((JSpinner) sandbox[index][0]);
					SpinnerNumberModel nm = (SpinnerNumberModel) js.getModel();
					js.setValue((int) (Math.random() * ((int) nm.getMaximum() - (int) nm.getMinimum() + 1)
							+ (int) nm.getMinimum()));
				}
			}
		}
	};

}
