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

	/**
	 * Constructor for objects of class Egg
	 */
	public Egg(Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints, double metabolism) {
		
		this.speed = speed;
		this.angle = angle;
		this.pos = pos;
		this.detectRadius = detectRadius;
		this.eggCycle = eggCycle;
		//this.accumulatedPoints = accumulatedPoints;
		this.timeBorn= GamePane.timeElapsed;
		this.carnivorePoints = carnivorePoints;
		this.metabolism = metabolism;
		while(Math.random()>.7){
			speed++;
		}
		while(Math.random()>.7){
			detectRadius += 3;
		}
		while(Math.random()>.7){
			metabolism+= 10.0;
		}
		if (carnivorePoints < 100){
			for (int x = 0; x < 3; x++){
				if(Math.random()>=.5)
					carnivorePoints ++ ;
				else
					carnivorePoints --;
			}
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
			if (carnivorePoints >= 100) {
				DrawArea.carnivores.add(new Carnivore(new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, 20000, metabolism));
			} else {
				DrawArea.herbivores.add(new Herbivore(new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, 20000, metabolism));
			}
			System.out.println("Egg hatched at" + GamePane.timeElapsed / 1000.0);
			return true;
		}
		return false;
	}
}
