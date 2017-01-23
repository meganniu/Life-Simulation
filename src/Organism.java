import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Organism {
	Rectangle hitbox;
	boolean selected = false;

	BufferedImage img;

	Point pos;

	double angle;

	int speed;// ticks/pixel

	protected int detectRadius;

	public Organism(Point pos, double angle, int speed, int detectRadius) {
		this.speed = speed;
		this.angle = angle;
		this.pos = pos;
		this.detectRadius = detectRadius;
		hitbox = new Rectangle(pos.x - 8, pos.y - 8, 16, 16);
	}

	public BufferedImage getImage() {
		return img;
	}

	public abstract double detectItem();

	public void move(int width, int height) {
		angle = detectItem();
		Point nextPos = nextPos(pos, angle);

		if (nextPos.x + 8 >= width) {
			// System.out.println("border encountered X");
			if (angle >= 270 && angle <= 360) {
				nextPos = nextPos(pos, 540 - angle);
				angle = 540 - angle;
			} else if (angle <= 90 && angle >= 0) {
				// System.out.print("HERE");
				nextPos = nextPos(pos, 180 - angle);
				angle = 180 - angle;
			}
		} else if (nextPos.x - 8 <= 0) {
			// System.out.println("border encountered X");
			if (angle >= 180 && angle <= 270) {
				nextPos = nextPos(pos, 540 - angle);
				angle = 540 - angle;
			} else if (angle < 180 && angle >= 90) {
				nextPos = nextPos(pos, 180 - angle);
				angle = 180 - angle;
			}
		}

		else if (nextPos.y + 8 >= height) {
			nextPos = nextPos(pos, 360 - angle);
			angle = 360 - angle;

			// nextPos.y = pos.y + (nextPos.y - pos.y) * -1;// disregarding cast
			// rule in this case
			// messes up the
			// path of org
			// Restore the cast
			// rule

		} else if (nextPos.y - 8 <= 0) {
			// System.out.println("border encountered Y, rejX:" + nextPos.x + "
			// rejY:" + nextPos.y);
			nextPos = nextPos(pos, 360 - angle);
			angle = 360 - angle;

		}
		pos.x = nextPos.x;
		pos.y = nextPos.y;

		hitbox.x = nextPos.x - 8;
		hitbox.y = nextPos.y - 8;
	}

	public Point nextPos(Point past, double angle) {
		// System.out.println("speed: " + speed);
		double run = speed * Math.cos(Math.toRadians(angle));
		// double run = speed * Math.cos(Math.toRadians(angle));
		// System.out.println("run: " + run);

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
		// double nextYDouble = Math.sin(Math.toRadians(angle)) * speed;//
		// expression
		// of
		// rise
		// in
		// terms
		// of
		// angle
		int nextY = (int) Math.round(nextYDouble);

		// System.out.println("nextY: " + nextY);

		if (nextY < 0 && (angle > 0 && angle < 180)) {// meaning bug will go
														// down
			// System.out.println("nextY: " + nextY);
			nextY = nextY * -1;
			// System.out.println("nextY: " + nextY);
		} else if (nextY > 0 && (angle > 180 && angle < 360)) {// meaning bug
																// will go down
			// System.out.println("nextY: " + nextY);
			nextY = nextY * -1;
			// System.out.println("nextY: " + nextY);
		}

		if (angle == 270.0) {
			nextY = -speed;// if angle is 270 move vertically down by 2
		} else if (angle == 90.0) { // for tan(x), 90 and 270 are asymptotes
			nextY = speed;// if angle is 90, move vertically up 2
		}

		Point nextPos = new Point(); // {nextX, nextY}
		/**
		 * if(angle == 90.0 || angle == 270.0){ nextPos.x = past.x;//traveling
		 * vertically, no change in x } else if((angle >= 0 && angle < 90.0) ||
		 * (angle >270.0 && angle <= 360.0)){ nextPos.x = past.x + 2; //moving 2
		 * pixels horizontally to the right } else{ nextPos.x = past.x - 2;
		 * //moving 2 pixels horizontally to the left }
		 **/
		// System.out.println("Rise: " + nextY + " Run: " + run);
		nextPos.x = past.x + (int) run;
		nextPos.y = past.y - nextY;// moving the organism vertically (direction
									// dep on earlier calcs)

		return nextPos;
	}

	public Point getPoint() {
		return pos;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle % 360;
	}

	public abstract ArrayList<String> getStats();
}
