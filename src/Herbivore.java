import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Herbivore extends Organism {
	
	private boolean chasing = false;
	
	public Herbivore() {
		super(pos, angle, minSpeed, restingSpeed, maxSpeed, speed, detectRadius, eggCycle, carnivorePoints, metabolism, energy);
		img = DrawArea.hImg;
	}


	public void setSelected(boolean b) {
		BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(DrawArea.hImg, 0, 0, null);
		selected = b;
		if (b) {
			Color green = new Color(0, 255, 0, 100);
			g.setColor(green);
			g.fillOval(0, 0, 48, 48);
			g.setColor(Color.green);
			g.drawOval(0, 0, 48, 48);
		}
		this.img = img;
	}

	public double detectItem() {

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

		if (shortestDistance == -1) {
			double shortestDistanceFood = -1;
			int indexOfClosestFood = -1;
			for (int i = 0; i < DrawArea.food.size(); i++) {
				Point hPoint = DrawArea.food.get(i).getPoint();
				double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
				if (distance < detectRadius && (distance < shortestDistanceFood || shortestDistanceFood == -1)) {
					shortestDistanceFood = distance;
					indexOfClosestFood = i;
				}
			}
			if (shortestDistanceFood == -1)
				return this.angle;
			else {
				double angle = Math.atan2(pos.y - DrawArea.food.get(indexOfClosestFood).getPoint().y,
						pos.x - DrawArea.food.get(indexOfClosestFood).getPoint().x);
				angle = Math.toDegrees(angle);

				if (angle >= 0 && angle <= 180) {
					angle = 180 - angle;
				} else if (angle >= -180 && angle <= 0) {
					angle = 180 - angle;
				}
				return angle;
			}

		} else {
			double angle = Math.atan2(pos.y - DrawArea.carnivores.get(indexOfClosest).getPoint().y,
					pos.x - DrawArea.carnivores.get(indexOfClosest).getPoint().x);
			angle = Math.toDegrees(angle);

			if (angle >= 0 && angle <= 180) {
				angle = 180 - angle;
			} else if (angle >= -180 && angle <= 0) {
				angle = 180 - angle;
			}

			angle = (angle + 180) % 360;
			return angle;
		}
	}

	public ArrayList<String> getStats() {
		ArrayList<String> stats = new ArrayList<String>();

		stats.add("<html><pre>Position\t(" + pos.x + ", " + pos.y + ")</pre></html>");
		stats.add("<html><pre>Angle\t\t" + (int) angle + " deg</pre></html>");
		stats.add("<html><pre>Speed\t\t" + speed + "</pre></html>");
		stats.add("<html><pre>R. Detection\t</pre></html>");
		stats.add("<html><pre>Egg Counter\t</pre></html>");
		stats.add("<html><pre>Generation\t</pre></html>");
		stats.add("<html><pre>Energy\t</pre></html>");

		return stats;

	}
	
	public void eat() {
		for (int i = 0; i < DrawArea.food.size(); i++) {
			Point hPoint = DrawArea.food.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance <= 24) {
				DrawArea.food.remove(i);
				i--;
			}
		}
	}
}



