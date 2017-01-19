import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Herbivore extends Organism {
	public Hitbox box;
	public Herbivore(Point pos, double angle, int speed, int detectRadius, int eggCycles, int gen, int energy) {
		super(pos, angle, speed, detectRadius, eggCycles, gen, energy);
		img = DrawArea.hImg;
		box = new Hitbox(pos.x - 8, pos.y - 8, 16, 16);
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
	
	public boolean eat() {
		for (int i = 0; i < DrawArea.food.size(); i++) {
			Point hPoint = DrawArea.food.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance <= 10) {
				DrawArea.food.remove(i);
				i--;
			}
		}
		return false;
	}
}
