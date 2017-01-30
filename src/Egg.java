import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Egg {
	private BufferedImage img;

	private Point pos;

	private Rectangle hitbox;

	private double angle;

	private int speed;// ticks/pixel
	protected int detectRadius;

	private int eggCycle;

	private long timeBorn;

	private double metabolism;

	private int carnivorePoints;

	private long chaseLength;
	
	private int dSpeed, dDR, dMetabolism, dCP, dCL, dEC;

	private int generation;

	public Egg(int generation, Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints,
			double metabolism, long chaseLength) {

		this.generation = generation;
		this.speed = speed;
		this.angle = angle;
		this.pos = pos;
		this.detectRadius = detectRadius;
		this.eggCycle = eggCycle;
		this.timeBorn = GamePane.timeElapsed;
		this.carnivorePoints = carnivorePoints;
		this.metabolism = metabolism;
		this.chaseLength = chaseLength;
		mutate();
		
		dSpeed = this.speed - speed;
		dDR = this.detectRadius - detectRadius;
		dMetabolism = (int) (this.metabolism - metabolism);
		dCP = this.carnivorePoints - carnivorePoints;
		dCL = (int) (this.chaseLength - chaseLength);
		dEC = this.eggCycle - eggCycle;
		
		img = DrawArea.eImg;
		hitbox = new Rectangle(pos.x - 8, pos.y - 8, 16, 16);
	}
	
	public void mutate() {
		while (Math.random() > .5) {
			speed++;
		}
		while (Math.random() > .5) {
			speed--;
		}
		while (Math.random() > .05) {
			detectRadius++;
		}
		while (Math.random() > .05) {
			detectRadius--;
		}
		while (Math.random() > .2) {
			metabolism++;
		}
		while (Math.random() > .2) {
			metabolism--;
		}
		while (Math.random() > .4) {
			carnivorePoints++;
		}
		while (Math.random() > .6) {
			carnivorePoints--;
		}
		while (Math.random() > .03) {
			chaseLength += 10;
		}
		while (Math.random() > .03) {
			chaseLength -= 10;
		}
		while (Math.random() > .2) {
			eggCycle += 50;
		}
		while (Math.random() > .2) {
			eggCycle -= 50;
		}
		if (speed < 2)
			speed = 2;
		if (detectRadius < 50)
			detectRadius = 50;
		if (eggCycle < 25000)
			eggCycle = 25000;
		if (carnivorePoints < 2)
			carnivorePoints = 2;
		if (metabolism < 60.0)
			metabolism = 60.0;
		if (chaseLength < 3500)
			chaseLength = 3500;
	}

	public BufferedImage getImage() {
		return img;
	}

	public Point getPoint() {
		return pos;
	}

	public double getAngle() {
		return angle;
	}
	
	public Rectangle getHitbox(){
		return hitbox;
	}

	public boolean hatch() {
		if (timeBorn + Main.hatchTime <= GamePane.timeElapsed) {
			if (carnivorePoints >= 10) {
				DrawArea.carnivores.add(new Carnivore(generation, new Point(pos), angle, speed, detectRadius, eggCycle,
						carnivorePoints, Main.newbornEnergy, metabolism, chaseLength));
			} else {
				DrawArea.herbivores.add(new Herbivore(generation, new Point(pos), angle, speed, detectRadius, eggCycle,
						carnivorePoints, Main.newbornEnergy, metabolism));
			}
			return true;
		}
		return false;
	}

	public ArrayList<String> getStats() {
		
		DecimalFormat df = new DecimalFormat("+#;-#");
		
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("<html><pre><span style=\"font-family: arial\">Egg\t\t");
		stats.add("<html><pre><span style=\"font-family: arial\">Generation\t\t" + generation + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Position\t\t(" + pos.x + ", " + pos.y
				+ ")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Angle\t\t" + (int) angle + " deg</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Speed\t\t" + speed + " ("+df.format(dSpeed)+")</span></pre></html>");
		stats.add(
				"<html><pre><span style=\"font-family: arial\">R. Detection\t" + detectRadius + " ("+df.format(dDR)+")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Egg Counter\t" + eggCycle + " ("+df.format(dEC)+")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Carnivorism\t" + carnivorePoints
				+ " ("+df.format(dCP)+")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Metabolism\t"
				+ new DecimalFormat("#.##").format(metabolism) + " ("+df.format(dMetabolism)+")</span></pre></html>");
		stats.add(
				"<html><pre><span style=\"font-family: arial\">Chase Length\t" + chaseLength + " ("+df.format(dCL)+")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Time Until Hatch\t"
				+ (timeBorn + 10000 - GamePane.timeElapsed) + "</span></pre></html>");
		return stats;
	}
}
