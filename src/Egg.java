import java.awt.Point;
import java.awt.image.BufferedImage;

public class Egg {
	BufferedImage img;

	Point pos;

	double angle;

	int speed;// ticks/pixel

	protected int detectRadius;

	int eggCycle;

	long timeBorn;

	double metabolism;
	
	int carnivorePoints;
	
	int accumulatedPoints;
	long chaseLength;
	/**
	 * Constructor for objects of class Egg
	 */
	public Egg(Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints, double metabolism, long chaseLength) {
		
		this.speed = speed;
		this.angle = angle;
		this.pos = pos;
		this.detectRadius = detectRadius;
		this.eggCycle = eggCycle;
		//this.accumulatedPoints = accumulatedPoints;
		this.timeBorn= GamePane.timeElapsed;
		this.carnivorePoints = carnivorePoints;
		this.metabolism = metabolism;
		this.chaseLength = chaseLength;
		while(Math.random()>.8){
			speed++;
		}
		while(Math.random()>.8){
			speed--;
		}
		while(Math.random()>.4){
			detectRadius ++;
		}
		while(Math.random()>.4){
			detectRadius --;
		}
		while(Math.random()>.2){
			metabolism ++;
		}
		while(Math.random()>.2){
			metabolism --;
		}
		while(Math.random()>.8){
			carnivorePoints++;
		}
		while(Math.random()>.8){
			carnivorePoints--;
		}
		while(Math.random()>.15){
			chaseLength += 10;
		}
		while(Math.random()>.15){
			chaseLength -= 10;
		}
		if (speed >= 9)
			detectRadius -= 20;
		img = DrawArea.eImg;
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

	public boolean hatch() {
		if (timeBorn + 5000 <= GamePane.timeElapsed) {
			if (carnivorePoints >= 10) {
				DrawArea.carnivores.add(new Carnivore(new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, 15000, metabolism, chaseLength));
			} else {
				DrawArea.herbivores.add(new Herbivore(new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, 15000, metabolism, 5000));
			}
			System.out.println("Egg hatched at" + GamePane.timeElapsed / 1000.0);
			return true;
		}
		return false;
	}
}
