import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Herbivore extends Organism {

	private boolean chasing = false;
	
	public Herbivore(Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints, double energy, double metabolism, long chaseLength) {
		super(pos, angle, speed, detectRadius, eggCycle, carnivorePoints, energy, metabolism, 5000);
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
	/*
	public Herbivore(Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints,
			int energy) {
		super(pos, angle, speed, detectRadius, eggCycle, carnivorePoints, energy);
		img = DrawArea.hImg;
	}
	*/

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

	public void layEgg(){
		if(GamePane.timeElapsed>sinceLastEgg+eggCycle && energy > 6000.0){
			sinceLastEgg=GamePane.timeElapsed;
			DrawArea.eggs.add(new Egg(new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, metabolism, chaseLength));
			System.out.println("Layed egg at " +GamePane.timeElapsed/1000.0);
			energy-=4000;
		}
	}
	
	public ArrayList<String> getStats() {
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("<html><pre><span style=\"font-family: arial\">Herbivore");
		stats.add("<html><pre><span style=\"font-family: arial\">Position\t\t(" + pos.x + ", " + pos.y + ")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Angle\t\t" + (int) angle + " deg</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Speed\t\t" + speed + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">R. Detection\t" +  detectRadius + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Egg Counter\t" + eggCycle + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Carnivorism\t" + carnivorePoints + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Energy\t" + new DecimalFormat("#.##").format(energy) + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Metabolism\t" + new DecimalFormat("#.##").format(metabolism) + "</span></pre></html>");

		return stats;

	}
	
	public void eat() {
		for (int i = 0; i < DrawArea.food.size(); i++) {
			Point hPoint = DrawArea.food.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance <= 24) {
				energy += (DrawArea.food.get(i).getNutrition() * metabolism / 100.0);
				if (energy > 15000.0)
					energy = 15000.0;
				if (DrawArea.food.get(i) == StatsPanel.selectedFood)
					StatsPanel.selectedFood = null;
				DrawArea.food.remove(i);
				i--;
			}
		}
	}
}



