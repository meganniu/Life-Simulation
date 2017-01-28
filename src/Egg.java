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

	int carnivorePoints;

	/**
	 * Constructor for objects of class Egg
	 */
	public Egg(Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints) {
		while(Math.random()>.7){
			speed++;
		}
		this.speed = speed;
		this.angle = angle;
		this.pos = pos;
		this.detectRadius = detectRadius;
		this.eggCycle = eggCycle;
		this.timeBorn= GamePane.timeElapsed;
		this.carnivorePoints = carnivorePoints;
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

	public void mutate() {
	}

	public boolean hatch() {
		if (timeBorn + 5000 <= GamePane.timeElapsed) {
			if (carnivorePoints >= 100) {
				DrawArea.carnivores.add(new Carnivore(new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, 20000, 100));
			} else {
				DrawArea.herbivores.add(new Herbivore(new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, 20000, 100));
			}
			System.out.println("Egg hatched at" + GamePane.timeElapsed / 1000.0);
			return true;
		}
		return false;
	}
}
