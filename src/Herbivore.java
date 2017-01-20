import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Herbivore extends Organism {
	public Herbivore(Point pos, double angle, int speed, int detectRadius) {
		super(pos, angle, speed, detectRadius);
		img = DrawArea.hImg;
	}

	public void setSelected(boolean b) {
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(DrawArea.hImg, 0, 0, null);
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

	public ArrayList<String> getStats() {
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

	public double detectCarnivore() {

		double shortestDistance = -1;
		int indexOfClosest = -1;
		for (int i = 0; i < DrawArea.carnivores.size(); i++) {
			Point hPoint = DrawArea.carnivores.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance < detectRadius && (distance < shortestDistance || shortestDistance == -1)) {
				shortestDistance = distance;
				indexOfClosest = i;
			}

		}
		if (shortestDistance == -1)
			return this.angle;
		else {
			double angle = Math.atan2(pos.y - DrawArea.carnivores.get(indexOfClosest).getPoint().y,
					pos.x - DrawArea.carnivores.get(indexOfClosest).getPoint().x);
			angle = Math.toDegrees(angle);

			if (angle >= 0 && angle <= 180) {
				angle = 180 - angle;
			} else if (angle >= -180 && angle <= 0) {
				angle = 180 - angle;
			}

			if (shortestDistance < detectRadius / 2) {
				angle = (angle + 180) % 360;
				return angle;
			} else {
				if (angle < (this.angle + 90) % 360 && angle > (this.angle - 90) % 360) {
					angle = (angle + 180) % 360;
					return angle;
				}
				else
					return this.angle;
			}

		}
	}
}
