import java.awt.Point;
import java.awt.image.BufferedImage;

public class Egg
{
	BufferedImage img;

	Point pos;

	double angle;

	int speed;// ticks/pixel

	protected int detectRadius;
	
	int eggCycle;
    /**
     * Constructor for objects of class Egg
     */
    public Egg(Point pos, double angle, int speed, int detectRadius, int eggCycle) {
		this.speed = speed;
		this.angle = angle;
		this.pos = pos;
		this.detectRadius = detectRadius;
		this.eggCycle = eggCycle;
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
}
