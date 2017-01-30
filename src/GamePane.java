import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.Rectangle;

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
	 * conditions set force by user
	 * automatically generated if not set
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
	 * @param width width of drawRegion
	 * @param height height of drawRegion
	 * @param startingCarnivores amount of carnivores to start
	 * @param startingHerbivores amount of herbivores to start
	 * @param startMinSpeed minimum starting speed
	 * @param startMaxSpeed maximum starting speed
	 * @param startMinRad minimum starting radius of detection
	 * @param startMaxRad maximum starting radius of detection
	 * @param startMinEgg minimum starting amount of eggs
	 * @param startMaxEgg maximum starting amount of eggs
	 * @param startMinEnergy minimum starting amount of energy
	 * @param startMaxEnergy maximum starting amount of energy
	 * @param startMinMetabolism minimum starting metabolism speed
	 * @param startMaxMetabolism maximum starting matabolism speed
	 * @param startMinFood minimum amount of starting food
	 * @param startMaxFood maximum amount of starting food
	 * @param chaseLength chase length for all organisms
	 */
	public GamePane(int width, int height, 
			int startingCarnivores, int startingHerbivores, 
			int startMinSpeed, int startMaxSpeed, 
			int startMinRad, int startMaxRad, 
			int startMinEgg, int startMaxEgg, 
			double startMinEnergy, double startMaxEnergy, 
			double startMinMetabolism, double startMaxMetabolism, 
			double startMinFood, double startMaxFood,
			long chaseLength) {
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
		
		drawArea = new DrawArea(startingCarnivores, startingHerbivores, 
				startMinSpeed, startMaxSpeed, 
				startMinRad, startMaxRad, 
				startMinEgg, startMaxEgg, 
				startMinEnergy, startMaxEnergy, 
				startMinMetabolism, startMaxMetabolism, 
				startMinFood, startMaxFood,
				chaseLength);

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
		if((finalOrg = drawArea.checkEnd()) != null && timeElapsed > 10){
			stop();
		}
		Main.statsPanel.updateStats();
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
		
		if(finalOrg != null){
			g.setColor(Color.white);
			g.fillRect((450) / 2, 150 / 2, 150, 250);
			
			g.setColor(Color.black);
			g.drawRect((450) / 2, 150 / 2, 150, 250);
			
			g.drawString("Simulation Over!", (500) / 2 + 6, (height - 400) / 2 + 10);
			g.drawString("Only one organism left", (500) / 2 - 12, (height - 400) / 2 + 25);
			ArrayList<String> rawStats = finalOrg.getFinalStats();
			
			for (int i = 0; i < rawStats.size(); i++){
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
		System.out.println(x + " " + y);
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
		}
		else if (eggFound){
			StatsPanel.selectedOrg = null;
			StatsPanel.selectedFood = null;
		}
		else if (foodFound){
			StatsPanel.selectedOrg = null;
			StatsPanel.selectedEgg = null;
		}
		else{
			StatsPanel.selectedOrg = null;
			StatsPanel.selectedEgg = null;
			StatsPanel.selectedFood = null;
		}
		Main.statsPanel.updateStats();
		
		if(Main.addingCarnivore){
			DrawArea.addCarnivore(new Point(x, y));
			Main.addingCarnivore = false;
			Main.addHerbivore.setEnabled(true);
		}
		if(Main.addingHerbivore){
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
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
