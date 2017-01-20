import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class DrawArea extends BufferedImage {

	static ArrayList<Carnivore> carnivores = new ArrayList<Carnivore>();
	static ArrayList<Herbivore> herbivores = new ArrayList<Herbivore>();
	static ArrayList<Food> food = new ArrayList<Food>();
	static ArrayList<Egg> eggs = new ArrayList<Egg>();
	static BufferedImage hImg = null, cImg = null;
	private Graphics2D g = null;
	static int width, height;

	public DrawArea() {
		super(1000, 1000, BufferedImage.TYPE_INT_ARGB);

		width = getWidth();
		height = getHeight();
		g = (Graphics2D) createGraphics();
		try {
			hImg = ImageIO.read(new File("images/herbivore.png"));
			cImg = ImageIO.read(new File("images/carnivore.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Herbivore her1 = new Herbivore(new Point(240, 100), 200, 20, 10);
		 * Herbivore her2 = new Herbivore(new Point(50, 200), 200, 15, 10);
		 * herbivores.add(her1); herbivores.add(her2); Carnivore car1 = new
		 * Carnivore(new Point(100, 100), 100, 10, 500); Carnivore car2 = new
		 * Carnivore(new Point(80, 32), 100, 18, 150); carnivores.add(car1);
		 */

		for (int i = 0; i < 15; i++) {
			herbivores.add(new Herbivore(new Point((int)(Math.random()*(1000-16)+8), (int)(Math.random()*(1000-16)+8)),Math.random()*360,(int)(Math.random()*5+10),(int)(Math.random()*200+50)));
		}
		for (int i = 0; i < 10; i++) {
			carnivores.add(new Carnivore(new Point((int)(Math.random()*(1000-16)+8), (int)(Math.random()*(1000-16)+8)),Math.random()*360,(int)(Math.random()*6+5),(int)(Math.random()*200+50)));
		}

		
		updateImage();
	}

	public void updatePositions() {
		for (int i = 0; i < carnivores.size(); i++) {
			carnivores.get(i).eat();
			double newAngle = carnivores.get(i).detectHerbivore();
			if (newAngle != -1) {
				carnivores.get(i).setAngle(newAngle);

			}
			carnivores.get(i).move(width, height);
		}

		for (int i = 0; i < herbivores.size(); i++) {
			herbivores.get(i).move(width, height);
			System.out.println(herbivores.get(i).pos.x + " "  + herbivores.get(i).pos.y);
		}
		updateImage();
	}

	public void updateImage() {
		/**
		 * Draws and paints background
		 */
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width , height );

		/**
		 * Rotates image of organism. Only works if image is a SQUARE.
		 * Otherwise, image will be cropped when rotated
		 */

		double locX = cImg.getWidth() / 2;
		double locY = cImg.getHeight() / 2;
		for (int i = 0; i < carnivores.size(); i++) {
			AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(360 - carnivores.get(i).getAngle()),
					locX, locY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter(carnivores.get(i).img, null), carnivores.get(i).getPoint().x - (cImg.getWidth() / 2),
					carnivores.get(i).getPoint().y - (cImg.getHeight() / 2), null);
		}
		for (int i = 0; i < herbivores.size(); i++) {
			AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(360 - herbivores.get(i).getAngle()),
					locX, locY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter(herbivores.get(i).img, null), herbivores.get(i).getPoint().x - (hImg.getWidth() / 2),
					herbivores.get(i).getPoint().y - (hImg.getHeight() / 2), null);
		}
	}
}
