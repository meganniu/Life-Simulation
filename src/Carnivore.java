import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Carnivore extends Organism {

	private boolean chasing = false;
	private boolean canChase = true;

	private long chaseStart;
	private long cooldownStart;

	public Carnivore(Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints, double energy, int metabolism) {
		super(pos, angle, speed, detectRadius, eggCycle, carnivorePoints, energy, metabolism);
		img = DrawArea.cImg;
	}

	public void setSelected(boolean b) {
		BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(DrawArea.cImg, 0, 0, null);
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
		for (int i = 0; i < DrawArea.herbivores.size(); i++) {
			Point hPoint = DrawArea.herbivores.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance < detectRadius && (distance < shortestDistance || shortestDistance == -1)) {
				shortestDistance = distance;
				indexOfClosest = i;
			}

		}
		if (shortestDistance == -1) {
			chasing = false;
			return this.angle;
		} else {

			if (!chasing){
				chaseStart = GamePane.timeElapsed;
				System.out.println("Chase has begun!" + GamePane.timeElapsed);
			}
			chasing = true;

			if (GamePane.timeElapsed < chaseStart + 5000 && canChase) {

				double angle = Math.atan2(pos.y - DrawArea.herbivores.get(indexOfClosest).getPoint().y,
						pos.x - DrawArea.herbivores.get(indexOfClosest).getPoint().x);
				angle = Math.toDegrees(angle);

				if (angle >= 0 && angle <= 180) {
					angle = 180 - angle;
				} else if (angle >= -180 && angle <= 0) {
					angle = 180 - angle;
				}

				/*
				 * double smoother = 0;
				 * 
				 * if(this.angle-angle<0) smoother = angle -
				 * Math.sqrt(angle-this.angle); else if(angle-this.angle<0)
				 * smoother = this.angle - Math.sqrt(this.angle-angle);
				 * 
				 * if (smoother >= 0 && smoother <= 180) { smoother = 180 -
				 * smoother; } else if (smoother >= -180 && smoother <= 0) {
				 * smoother = 180 - smoother; }
				 * 
				 * return smoother;
				 */

				return angle;
			} else {
				chaseStart = GamePane.timeElapsed;
				if (canChase){
					canChase = false;
					cooldownStart = GamePane.timeElapsed;
					System.out.println("End of chase" + GamePane.timeElapsed);
					return (this.angle + Math.random() * 91 - 45) % 360;
				}
				else{
					if(GamePane.timeElapsed > cooldownStart + 4000){
						canChase = true;
						System.out.println("Can chase" + GamePane.timeElapsed);
					}
					return this.angle;
				}
				
				
			}

		}
	}

	public boolean isChasing() {
		return chasing;
	}

	public void eat() {
		for (int i = 0; i < DrawArea.herbivores.size(); i++) {
			Point hPoint = DrawArea.herbivores.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance <= 24) {
				DrawArea.herbivores.remove(i);
				i--;
			}
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

}
