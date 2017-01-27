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
	static BufferedImage hImg = null, cImg = null, eImg = null, fImg = null;
	private Graphics2D g = null;
	static int width, height;

	public DrawArea() {
		super(2000, 2000, BufferedImage.TYPE_INT_ARGB);

		width = getWidth();
		height = getHeight();
		
		System.out.println("width: " + width);
		System.out.println("height: " + height);
		
		g = (Graphics2D) createGraphics();
		try {
			hImg = ImageIO.read(new File("images/herbivore.png"));
			cImg = ImageIO.read(new File("images/carnivore.png"));
			eImg = ImageIO.read(new File("images/egg.png"));
			fImg = ImageIO.read(new File("images/food.png"));
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

		for (int i = 0; i < 50; i++) {
			herbivores.add(new Herbivore(new Point((int)(Math.random()*(1000-16)+8), (int)(Math.random()*(1000-16)+8)),Math.random()*360.0,(int)(Math.random()*10+5),(int)(Math.random()*60+20), (int)(Math.random()*1000+50000), 20, 20000));
		}
		for (int i = 0; i < 1; i++) {
			carnivores.add(new Carnivore(new Point((int)(Math.random()*(1000-16)+8), (int)(Math.random()*(1000-16)+8)),Math.random()*360.0,(int)(Math.random()*10+5),(int)(Math.random()*80+100), (int)(Math.random()*1000+5000), 120, 20000));
		}
	}

	public void updatePositions() {
		for (int i = 0; i < carnivores.size(); i++) {
			carnivores.get(i).eat();
			carnivores.get(i).move(width, height);
		}

		for (int i = 0; i < herbivores.size(); i++) {
			herbivores.get(i).move(width, height);
		}
	}
	
	public void layEggs(){
		for (int i = 0; i < carnivores.size(); i++) {
			carnivores.get(i).layEgg();
		}

		for (int i = 0; i < herbivores.size(); i++) {
			herbivores.get(i).layEgg();
		}
	}
	
	public void hatchEggs(){
		for (int i = 0; i < eggs.size(); i++){
			if(eggs.get(i).hatch()){
				eggs.remove(i);
				i--;
			}
		}
	}
	
	public void energyCheck(){
		for (int i = 0; i < carnivores.size(); i++){
			carnivores.get(i).energyUse();
			if (carnivores.get(i).getEnergy() <= 0){
				carnivores.remove(i);
				i--;
				System.out.println("Carnivore died");
			}
		}
		for (int i = 0; i < herbivores.size(); i++){
			herbivores.get(i).energyUse();
			if (herbivores.get(i).getEnergy() <= 0){
				herbivores.remove(i);
				i--;
				System.out.println("Herbivore died");
			}
		}
	}
	
	public void spawnFood(){
		if (Math.random()<0.2){
			food.add(new Food(4000, new Point((int)(Math.random()*(1000-16)+8), (int)(Math.random()*(1000-16)+8))));
			System.out.println("Spawned a food");
		}
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
		for (int i = 0; i < eggs.size(); i++) {
			g.drawImage(eggs.get(i).getImage(), eggs.get(i).getPoint().x-8, eggs.get(i).getPoint().y-8, null);
		}
		for (int i = 0; i < food.size(); i++) {
			g.drawImage(food.get(i).getImage(), food.get(i).getPoint().x-8, food.get(i).getPoint().y-8, null);
		}
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

