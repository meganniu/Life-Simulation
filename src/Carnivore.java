import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Carnivore extends Organism {

	public Carnivore(Point pos, double angle, int speed, int detectRadius) {
		super(pos, angle, speed, detectRadius);
		img = DrawArea.cImg;
	}

	public void setSelected(boolean b) {
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(DrawArea.cImg, 0, 0, null);
		selected = b;
		if (b) {
			Color green = new Color(0, 255, 0, 100);
			g.setColor(green);
			g.fillOval(0, 0, 24, 24);
			g.setColor(Color.green);
			g.drawOval(0, 0, 24, 24);
		}
		this.img = img;
	}

	public double detectHerbivore() {

		double shortestDistance = -1;
		int indexOfClosest = -1;
		for (int i = 0; i < DrawArea.herbivores.size(); i++) {
			Point hPoint = DrawArea.herbivores.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance < detectRadius && (distance < shortestDistance || shortestDistance == -1)) {
				shortestDistance = distance;
				indexOfClosest = i;
			}

		}
		if (shortestDistance == -1)
			return this.angle;
		else {
			// int deltaX = ;
			// int deltaY;

			double angle = Math.atan2(pos.y - DrawArea.herbivores.get(indexOfClosest).getPoint().y,
					pos.x - DrawArea.herbivores.get(indexOfClosest).getPoint().x);
			angle = Math.toDegrees(angle);

			if (angle >= 0 && angle <= 180) {
				angle = 180 - angle;
			} else if (angle >= -180 && angle <= 0) {
				angle = 180 - angle;
			}

			return angle;
		}
	}

	public boolean eat() {
		for (int i = 0; i < DrawArea.herbivores.size(); i++) {
			Point hPoint = DrawArea.herbivores.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance <= 12) {
				DrawArea.herbivores.remove(i);
				i--;
			}
		}
		return false;
	}
	
	public ArrayList<String> getStats(){
		ArrayList<String> stats = new ArrayList<String>();
		
		stats.add("Position		(" + pos.x + ", " + pos.y + ")");
		stats.add("Angle		" + angle + " deg");
		stats.add("Speed		" + speed + "");
		stats.add("R. Detection	");
		stats.add("Egg Counter	");
		stats.add("Generation	");
		stats.add("Energy		");
		
		return stats;
		
	}

}
