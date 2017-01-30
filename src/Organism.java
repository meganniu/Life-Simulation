import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Parent class of herbivores and carnivores
 */
public abstract class Organism {

	/**
	 * hitbox of organism used to detect when objects overlap
	 */
	private Rectangle hitbox;
	
	/**
	 * image of organism
	 */
	protected BufferedImage img;
	
	/**
	 * stats of organism
	 */
	protected Point pos;
	protected double angle;
	protected int speed;// ticks/pixel
	protected int detectRadius;
	protected double metabolism;
	protected int carnivorePoints;
	protected double energy;
	protected long sinceLastEgg;
	protected long chaseLength;
	protected int eggCycle;

	/**
	 * previous few positions of organism, used for drawing trails
	 */
	public ArrayList<Point> prevPoints = new ArrayList<Point>();

	/**
	 * Organism constructor
	 * @param pos position of organism
	 * @param angle angle of movement
	 * @param speed speed of organism
	 * @param detectRadius detection radius of organism
	 * @param eggCycle egg cycle time of organism
	 * @param carnivorePoints carnivore points of organism
	 * @param energy energy of organism
	 * @param metabolism matabolism rate of organism
	 * @param chaseLength maximum chase length of organism
	 */
	public Organism(Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints,
			double energy, double metabolism, long chaseLength) {
		this.speed = speed;
		this.angle = angle;
		this.pos = pos;
		this.detectRadius = detectRadius;
		this.eggCycle = eggCycle;
		this.carnivorePoints = carnivorePoints;
		this.metabolism = metabolism;
		this.energy = energy;
		this.speed = speed;
		this.chaseLength = chaseLength;
		sinceLastEgg = GamePane.timeElapsed;
		hitbox = new Rectangle(pos.x - 16, pos.y - 16, 32, 32);

		prevPoints.add(pos);
	}

	/**
	 * Returns image of organism
	 * @return image of organism
	 */
	public BufferedImage getImage() {
		return img;
	}

	/**
	 * Detection of objects based on detection radius and relative proximity 
	 * @return detected item
	 */
	public abstract double detectItem();

	/**
	 * Eat object if encountered
	 */
	public abstract void eat();

	/**
	 * Calculate next position of organism and change angle if wall is ecountered
	 * @param width width of display area of simulation
	 * @param height height of display area of simulation
	 */
	public void move(int width, int height) {
		angle = detectItem();
		Point nextPos = nextPos(pos, angle);

		if (nextPos.x + 16 >= width) {
			if (angle >= 270 && angle <= 360) {
				nextPos = nextPos(pos, 540 - angle);
				angle = 540 - angle;
			} else if (angle <= 90 && angle >= 0) {
				nextPos = nextPos(pos, 180 - angle);
				angle = 180 - angle;
			}
		} else if (nextPos.x - 16 <= 0) {
			if (angle >= 180 && angle <= 270) {
				nextPos = nextPos(pos, 540 - angle);
				angle = 540 - angle;
			} else if (angle < 180 && angle >= 90) {
				nextPos = nextPos(pos, 180 - angle);
				angle = 180 - angle;
			}
		}

		else if (nextPos.y + 16 >= height) {
			nextPos = nextPos(pos, 360 - angle);
			angle = 360 - angle;

		} else if (nextPos.y - 16 <= 0) {
			nextPos = nextPos(pos, 360 - angle);
			angle = 360 - angle;

		}

		pos.x = nextPos.x;
		pos.y = nextPos.y;

		if (prevPoints.size() >= 15)
			prevPoints.remove(0);
		if (GamePane.tickCounter % 3 != 0)
			prevPoints.add(nextPos);
		else
			prevPoints.add(null);

		hitbox.x = nextPos.x - 8;
		hitbox.y = nextPos.y - 8;
	}

	/**
	 * Calculate next position of organism
	 * @param past last position
	 * @param angle current angle
	 * @return next position
	 */
	public Point nextPos(Point past, double angle) {
		double run = speed * Math.cos(Math.toRadians(angle));

		if (run < 0 && ((angle >= 0 && angle <= 90) || angle <= 360 && angle >= 270)) {
			run *= -1;
		} else if (run > 0 && (angle >= 90 && angle <= 270)) {
			run *= -1;
		}

		/**
		 * using tan(x) = rise/run assuming run is always 2 (i.e. moving 2
		 * pixels horizontally each time), rise (change in vertical movement)
		 * can be expressed as rise = 2tan(x) where x is the angle of movement
		 */

		double nextYDouble = speed * Math.sin(Math.toRadians(angle));
		int nextY = (int) Math.round(nextYDouble);

		if (nextY < 0 && (angle > 0 && angle < 180)) {// meaning bug will go
														// down
			nextY = nextY * -1;
		} else if (nextY > 0 && (angle > 180 && angle < 360)) {// meaning bug
																// will go down
			nextY = nextY * -1;
		}

		if (angle == 270.0) {
			nextY = -speed;// if angle is 270 move vertically down by 2
		} else if (angle == 90.0) { // for tan(x), 90 and 270 are asymptotes
			nextY = speed;// if angle is 90, move vertically up 2
		}

		Point nextPos = new Point(); // {nextX, nextY}
		nextPos.x = past.x + (int) run;
		nextPos.y = past.y - nextY;// moving the organism vertically (direction
									// dep on earlier calcs)

		return nextPos;
	}

	/**
	 * Get position of organism
	 * @return position of organism
	 */
	public Point getPoint() {
		return pos;
	}

	/**
	 * Get speed of organism
	 * @return speed of organism
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Get detection radius of organism
	 * @return detection radius of organism
	 */
	public int getDetectRadius() {
		return detectRadius;
	}

	/**
	 * Get angle of organism
	 * @return angle of organism
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Get energy of organism
	 * @return energy of organism
	 */
	public double getEnergy() {
		return energy;
	}

	/**
	 * Change angle of organism
	 * @param angle angle to change to
	 */
	public void setAngle(double angle) {
		this.angle = angle % 360;
	}
	
	/**
	 * Get hitbox of organism
	 * @return hitbox of organism
	 */
	public Rectangle getHitbox(){
		return hitbox;
	}

	/**
	 * Decrease energy based on energy use
	 */
	public void energyUse(){
		energy-=1; // Passive energy loss
		energy-=(speed*metabolism /80.0);
		if (energy < 0)
			energy = 0;
	}
	
	/**
	 * Lay egg if ready
	 */
	public abstract void layEgg();
	
	/**
	 * Return ArrayList of stats formatted in html
	 * @return ArrayList of stats formatted in html
	 */
	public abstract ArrayList<String> getFinalStats();
	
	/**
	 * Return ArrayList of stats
	 * @return ArrayList of stats
	 */
	public abstract ArrayList<String> getStats();
}
