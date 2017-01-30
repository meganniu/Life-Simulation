import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * Egg object which hatched into an organism with evolved traits
 */
public class Egg {
	/**
	 * egg image
	 */
	private BufferedImage img;

	/**
	 * position of egg
	 */
	private Point pos;
	
	/**
	 * rectangle used as hitbox
	 */
	Rectangle hitbox;
	
	/**
	 * angle at which resulting organism will travel
	 */
	private double angle;
	
	/**
	 * speed at which resulting organism will travel
	 */
	private int speed;// ticks/pixel
	
	/**
	 * detect radius of resulting organism
	 */
	protected int detectRadius;
	
	/**
	 * incubation time of egg
	 */
	private int eggCycle;
	
	/**
	 * time when organism is born
	 */
	private long timeBorn;
	
	/**
	 * metabolism rate of resulting organism
	 */
	private double metabolism;
	
	/**
	 * carnivore points of resulting organism
	 */
	private int carnivorePoints;
	
	/**
	 * chaselength of resutling organism
	 */
	private long chaseLength;

	/**
	 * Egg constructor
	 * @param pos position of parent
	 * @param angle angle of parent
	 * @param speed speed of parent
	 * @param detectRadius detect radius of parent
	 * @param eggCycle eggCycle of parent
	 * @param carnivorePoints carnivorePoints of parent
	 * @param metabolism metabolism of parent
	 * @param chaseLength chase length of paretn
	 */
	public Egg(Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints, double metabolism, long chaseLength) {
		
		this.speed = speed;
		this.angle = angle;
		this.pos = pos;
		this.detectRadius = detectRadius;
		this.eggCycle = eggCycle;
		this.timeBorn= GamePane.timeElapsed;
		this.carnivorePoints = carnivorePoints;
		this.metabolism = metabolism;
		this.chaseLength = chaseLength;
		mutate();
		img = DrawArea.eImg;
		hitbox = new Rectangle(pos.x - 8, pos.y - 8, 16, 16);
	}
	
	/**
	 * evolved the stats of egg based on stats of parent
	 */
	public void mutate(){
		while(Math.random()>.5){
			speed++;
		}
		while(Math.random()>.5){
			speed--;
		}
		while(Math.random()>.05){
			detectRadius ++;
		}
		while(Math.random()>.05){
			detectRadius --;
		}
		while(Math.random()>.2){
			metabolism ++;
		}
		while(Math.random()>.2){
			metabolism --;
		}
		while(Math.random()>.6){
			carnivorePoints++;
		}
		while(Math.random()>.6){
			carnivorePoints--;
		}
		while(Math.random()>.2){
			chaseLength += 10;
		}
		while(Math.random()>.2){
			chaseLength -= 10;
		}
		while(Math.random()>.2){
			eggCycle += 50;
		}
		while(Math.random()>.2){
			eggCycle -= 50;
		}
		if (speed >= 10)
			detectRadius =  detectRadius / 3;
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
	
	/**
	 * get image of egg
	 * @return image of egg
	 */
	public BufferedImage getImage() {
		return img;
	}

	/**
	 * get position of egg
	 * @return position of egg
	 */
	public Point getPoint() {
		return pos;
	}

	/**
	 * get angle of resulting organism
	 * @return angle of resulting organism
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * hatch egg when incubation time is over
	 * @return if egg is ready to hatch
	 */
	public boolean hatch() {
		if (timeBorn + Main.hatchTime <= GamePane.timeElapsed) {
			if (carnivorePoints >= 10) {
				DrawArea.carnivores.add(new Carnivore(new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, Main.newbornEnergy, metabolism, chaseLength));
			} else {
				DrawArea.herbivores.add(new Herbivore(new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, Main.newbornEnergy, metabolism, 5000));
			}
			return true;
		}
		return false;
	}
	
	/**
	 * stats of egg in html formatting
	 * @return stats of egg in html
	 */
	public ArrayList<String> getStats() {
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("<html><pre><span style=\"font-family: arial\">Egg\t\t");
		stats.add("<html><pre><span style=\"font-family: arial\">Position\t\t(" + pos.x + ", " + pos.y + ")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Angle\t\t" + (int) angle + " deg</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Speed\t\t" + speed + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">R. Detection\t" +  detectRadius + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Egg Counter\t" + eggCycle + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Carnivorism\t" + carnivorePoints + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Metabolism\t" + new DecimalFormat("#.##").format(metabolism) + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Chase Length\t" + chaseLength + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Time Until Hatch\t" + (timeBorn + 10000 - GamePane.timeElapsed) + "</span></pre></html>");
		return stats;
	}
}
